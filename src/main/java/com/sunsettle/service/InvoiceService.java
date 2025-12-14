package com.sunsettle.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;

import com.sunsettle.dto.BillDTO;
import com.sunsettle.entity.Invoice;
import com.sunsettle.entity.Site;
import com.sunsettle.repository.InvoiceRepository;
import com.sunsettle.repository.SiteRepository;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

        // 1) Generate BillDTO
        BillDTO bill = billingService.generateBill(siteId, month, year);

        // 2) Fetch site info
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));

        // 3) Generate PDF
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        // HEADER
        Font headerFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph header = new Paragraph("SUNSETTLE ENERGY PRIVATE LIMITED", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        Paragraph sub = new Paragraph("Solar PPA Monthly Invoice", new Font(Font.HELVETICA, 13));
        sub.setAlignment(Element.ALIGN_CENTER);
        document.add(sub);

        document.add(new Paragraph("\n"));

        // BILL INFO
        Font label = new Font(Font.HELVETICA, 11, Font.BOLD);

        document.add(new Paragraph("Invoice No: INV-" + System.currentTimeMillis(), label));
        document.add(new Paragraph("Invoice Date: " + java.time.LocalDate.now()));
        document.add(new Paragraph("Billing Period: " + bill.getMonth()));
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Site: " + site.getSiteName(), label));
        document.add(new Paragraph("Location: " + site.getLocation()));
        document.add(new Paragraph("Tariff: ₹" + bill.getTariff() + "/kWh"));
        document.add(new Paragraph("\n"));

        // TABLE
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        Font tableHeader = new Font(Font.HELVETICA, 12, Font.BOLD);

        PdfPCell h1 = new PdfPCell(new Paragraph("Description", tableHeader));
        PdfPCell h2 = new PdfPCell(new Paragraph("Amount", tableHeader));

        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(h1);
        table.addCell(h2);

        table.addCell("Exported Energy (kWh)");
        table.addCell(String.valueOf(bill.getExportKwh()));

        table.addCell("Energy Charges (₹)");
        table.addCell(String.valueOf(bill.getEnergyCharge()));

        table.addCell("GST @ 18% (₹)");
        table.addCell(String.valueOf(bill.getGst18()));

        PdfPCell totalDesc = new PdfPCell(new Paragraph("Total Payable (₹)", tableHeader));
        PdfPCell totalVal = new PdfPCell(new Paragraph(String.valueOf(bill.getTotalPayable()), tableHeader));

        totalDesc.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalVal.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(totalDesc);
        table.addCell(totalVal);

        document.add(table);

        // FOOTER
        Paragraph footer = new Paragraph(
                "This is a system-generated invoice. For support: support@sunsettle.com"
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(20);
        document.add(footer);

        document.close();

        // SAVE INVOICE IN DATABASE
        Invoice invoice = Invoice.builder()
                .siteId(siteId)
                .siteName(site.getSiteName())
                .month(month + "-" + year)   // string month
                .monthNumber(month)          // numeric month
                .year(year)
                .exportKwh(bill.getExportKwh())
                .energyCharge(bill.getEnergyCharge())
                .gst18(bill.getGst18())
                .totalPayable(bill.getTotalPayable())
                .pdfFileName("invoice_" + year + "_" + month + "_site" + siteId + ".pdf")
                .createdAt(LocalDateTime.now())
                .build();

        invoiceRepository.save(invoice);

        return new ByteArrayInputStream(out.toByteArray());
    }
}
