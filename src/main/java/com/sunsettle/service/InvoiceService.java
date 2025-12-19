package com.sunsettle.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import com.sunsettle.dto.BillDTO;
import com.sunsettle.entity.Invoice;
import com.sunsettle.entity.Site;
import com.sunsettle.repository.InvoiceRepository;
import com.sunsettle.repository.SiteRepository;

import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class InvoiceService {

    private final BillingService billingService;
    private final InvoiceRepository invoiceRepository;
    private final SiteRepository siteRepository;

    public InvoiceService(BillingService billingService,
                          InvoiceRepository invoiceRepository,
                          SiteRepository siteRepository) {
        this.billingService = billingService;
        this.invoiceRepository = invoiceRepository;
        this.siteRepository = siteRepository;
    }

    public ByteArrayInputStream generateInvoice(Long siteId, int month, int year) throws Exception {

        // --------------------------------------------------
        // 1️⃣ BUSINESS DATA
        // --------------------------------------------------
        BillDTO bill = billingService.generateBill(siteId, month, year);

        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));

        // Round values (professional invoices never show long decimals)
        double exportKwh = round(bill.getExportKwh());
        double energyCharge = round(bill.getEnergyCharge());
        double gst = round(bill.getGst18());
        double total = round(bill.getTotalPayable());

        // --------------------------------------------------
        // 2️⃣ PDF SETUP
        // --------------------------------------------------
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font title = new Font(Font.HELVETICA, 15, Font.BOLD);
        Font bold = new Font(Font.HELVETICA, 10, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 10);
        Font small = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.DARK_GRAY);

        // --------------------------------------------------
        // 3️⃣ HEADER (LOGO + COMPANY)
        // --------------------------------------------------
        PdfPTable header = new PdfPTable(2);
        header.setWidthPercentage(100);
        header.setWidths(new float[]{1.2f, 3.8f});

        Image logo = Image.getInstance(
                getClass().getResource("/static/logo.png")
        );
        logo.scaleToFit(110, 55);

        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell companyCell = new PdfPCell();
        companyCell.setBorder(Rectangle.NO_BORDER);
        companyCell.addElement(new Paragraph("SUNSETTLE ENERGY PRIVATE LIMITED", title));
        companyCell.addElement(new Paragraph(
                "GSTIN: 06ABCDE1234F1Z5\nsupport@sunsettle.com\nwww.sunsettle.com",
                small
        ));

        header.addCell(logoCell);
        header.addCell(companyCell);
        document.add(header);

        document.add(new Paragraph("\n"));

        // --------------------------------------------------
        // 4️⃣ INVOICE META (RIGHT BOX)
        // --------------------------------------------------
        PdfPTable meta = new PdfPTable(2);
        meta.setWidthPercentage(45);
        meta.setHorizontalAlignment(Element.ALIGN_RIGHT);

        addMeta(meta, "Invoice No", "INV-" + System.currentTimeMillis(), bold, normal);
        addMeta(meta, "Invoice Date", LocalDate.now().toString(), bold, normal);
        addMeta(meta, "Billing Period", month + "-" + year, bold, normal);

        document.add(meta);
        document.add(new Paragraph("\n"));

        // --------------------------------------------------
        // 5️⃣ BILL TO
        // --------------------------------------------------
        document.add(new Paragraph("BILL TO:", bold));
        document.add(new Paragraph(site.getClient().getName(), normal));
        document.add(new Paragraph(site.getLocation(), normal));
        document.add(new Paragraph("Site: " + site.getSiteName(), normal));
        document.add(new Paragraph("\n"));

        // --------------------------------------------------
        // 6️⃣ CHARGES TABLE
        // --------------------------------------------------
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 5, 2, 2});

        addHeader(table, "Sr");
        addHeader(table, "Description");
        addHeader(table, "Units");
        addHeader(table, "Amount (₹)");

        addRow(table, "1", "Exported Energy Charges",
                exportKwh + " kWh", energyCharge);

        addRow(table, "2", "GST @ 18%", "", gst);

        PdfPCell totalLabel = new PdfPCell(new Phrase("TOTAL PAYABLE", bold));
        totalLabel.setColspan(3);
        totalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell totalValue = new PdfPCell(
                new Phrase("₹ " + total, bold)
        );
        totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(totalLabel);
        table.addCell(totalValue);

        document.add(table);

        // --------------------------------------------------
        // 7️⃣ FOOTER
        // --------------------------------------------------
        Paragraph footer = new Paragraph(
                "\nThis is a system generated invoice.\n" +
                "Payment due within 15 days.\n\n" +
                "Authorized Signatory",
                small
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();

        // --------------------------------------------------
        // 8️⃣ SAVE INVOICE
        // --------------------------------------------------
        Invoice invoice = Invoice.builder()
                .siteId(siteId)
                .siteName(site.getSiteName())
                .month(month + "-" + year)
                .monthNumber(month)
                .year(year)
                .exportKwh(exportKwh)
                .energyCharge(energyCharge)
                .gst18(gst)
                .totalPayable(total)
                .pdfFileName("invoice_" + year + "_" + month + "_site" + siteId + ".pdf")
                .createdAt(LocalDateTime.now())
                .clientId(site.getClient().getId())
                .build();

        invoiceRepository.save(invoice);

        return new ByteArrayInputStream(out.toByteArray());
    }

    // --------------------------------------------------
    // 🔧 HELPERS
    // --------------------------------------------------
    private void addMeta(PdfPTable table, String key, String value,
                         Font bold, Font normal) {
        PdfPCell k = new PdfPCell(new Phrase(key, bold));
        PdfPCell v = new PdfPCell(new Phrase(value, normal));
        k.setBorder(Rectangle.NO_BORDER);
        v.setBorder(Rectangle.NO_BORDER);
        table.addCell(k);
        table.addCell(v);
    }

    private void addHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, new Font(Font.HELVETICA, 10, Font.BOLD))
        );
        cell.setBackgroundColor(new Color(230, 230, 230));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addRow(PdfPTable table, String sr, String desc,
                        String units, double amount) {
        table.addCell(sr);
        table.addCell(desc);
        table.addCell(units);
        table.addCell("₹ " + amount);
    }

    private double round(double val) {
        return BigDecimal.valueOf(val)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
