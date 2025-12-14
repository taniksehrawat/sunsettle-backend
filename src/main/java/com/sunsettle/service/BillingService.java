package com.sunsettle.service;

import com.sunsettle.dto.BillDTO;
import com.sunsettle.entity.Invoice;
import com.sunsettle.entity.MeterReading;
import com.sunsettle.entity.Tariff;
import com.sunsettle.repository.InvoiceRepository;
import com.sunsettle.repository.MeterReadingRepository;
import com.sunsettle.repository.TariffRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillingService {

    private final MeterReadingRepository meterReadingRepository;
    private final TariffRepository tariffRepository;
    private final InvoiceRepository invoiceRepository;

    public BillingService(
            MeterReadingRepository meterReadingRepository,
            TariffRepository tariffRepository,
            InvoiceRepository invoiceRepository
    ) {
        this.meterReadingRepository = meterReadingRepository;
        this.tariffRepository = tariffRepository;
        this.invoiceRepository = invoiceRepository;
    }

    /* -------------------------------------------------------------
       GENERATE BILL (USED BY ADMIN & PDF GENERATOR)
       ------------------------------------------------------------- */
    public BillDTO generateBill(Long siteId, int month, int year) {

        Tariff tariff = tariffRepository.findBySiteId(siteId);
        if (tariff == null)
            throw new RuntimeException("Tariff not set for this site");

        List<MeterReading> readings = meterReadingRepository.findBySiteId(siteId);

        double totalExport = readings.stream()
                .filter(r -> r.getReadingDate().getMonthValue() == month
                        && r.getReadingDate().getYear() == year)
                .mapToDouble(MeterReading::getExportKwh)
                .sum();

        double energyCharge = totalExport * tariff.getRatePerKwh();
        double gst = energyCharge * 0.18;
        double total = energyCharge + gst;

        return BillDTO.builder()
                .siteId(siteId)
                .month(month + "-" + year)
                .monthNumber(month)
                .year(year)
                .exportKwh(totalExport)
                .tariff(tariff.getRatePerKwh())
                .energyCharge(energyCharge)
                .gst18(gst)
                .totalPayable(total)
                .build();
    }

    /* -------------------------------------------------------------
       SAVE INVOICE IN DATABASE AFTER PDF GENERATION
       ------------------------------------------------------------- */
    public Invoice saveInvoice(BillDTO bill, Long clientId, String siteName) {

        Invoice invoice = Invoice.builder()
                .siteId(bill.getSiteId())
                .siteName(siteName)

                .month(bill.getMonth())
                .monthNumber(bill.getMonthNumber())
                .year(bill.getYear())

                .exportKwh(bill.getExportKwh())
                .energyCharge(bill.getEnergyCharge())
                .gst18(bill.getGst18())
                .totalPayable(bill.getTotalPayable())

                .clientId(clientId)
                .createdAt(java.time.LocalDateTime.now())
                .pdfFileName("invoice_" + bill.getYear() + "_" + bill.getMonthNumber() + "_site" + bill.getSiteId() + ".pdf")
                .build();

        return invoiceRepository.save(invoice);
    }

    /* -------------------------------------------------------------
       GET BILL HISTORY FOR A CLIENT
       ------------------------------------------------------------- */
    public List<BillDTO> getBillsByClient(Long clientId) {

        List<Invoice> invoices = invoiceRepository.findByClientId(clientId);

        List<BillDTO> bills = new ArrayList<>();

        for (Invoice inv : invoices) {

            bills.add(
                    BillDTO.builder()
                            .siteId(inv.getSiteId())
                            .siteName(inv.getSiteName())

                            .month(inv.getMonth())
                            .monthNumber(inv.getMonthNumber())
                            .year(inv.getYear())

                            .exportKwh(inv.getExportKwh())
                            .energyCharge(inv.getEnergyCharge())
                            .gst18(inv.getGst18())
                            .totalPayable(inv.getTotalPayable())
                            .build()
            );
        }

        return bills;
    }
}
