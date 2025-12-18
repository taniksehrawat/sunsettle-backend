package com.sunsettle.controller;

import com.sunsettle.dto.BillDTO;
import com.sunsettle.service.BillingService;
import com.sunsettle.service.InvoiceService;
import com.sunsettle.service.TariffService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillingController {

    private final BillingService billingService;
    private final TariffService tariffService;
    private final InvoiceService invoiceService;

    public BillingController(BillingService billingService,
                             TariffService tariffService,
                             InvoiceService invoiceService) {
        this.billingService = billingService;
        this.tariffService = tariffService;
        this.invoiceService = invoiceService;
    }

    /*--------------------------------------------------------------
     * 1️⃣ SET TARIFF (₹/kWh)
     *--------------------------------------------------------------*/
    @PostMapping("/tariff/{siteId}")
    public String setTariff(
            @PathVariable Long siteId,
            @RequestParam Double rate
    ) {
        tariffService.setTariff(siteId, rate);
        return "Tariff set successfully @ ₹" + rate + "/kWh";
    }

    /*--------------------------------------------------------------
     * 2️⃣ GENERATE BILL SUMMARY (JSON)
     *--------------------------------------------------------------*/
    @GetMapping("/bill/{siteId}")
    public BillDTO generateBill(
            @PathVariable Long siteId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return billingService.generateBill(siteId, month, year);
    }

    /*--------------------------------------------------------------
     * 3️⃣ GENERATE & DOWNLOAD INVOICE PDF
     *--------------------------------------------------------------*/
    @GetMapping("/invoice/{siteId}")
    public ResponseEntity<byte[]> downloadInvoicePdf(
            @PathVariable Long siteId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        try {
            ByteArrayInputStream pdf =
                    invoiceService.generateInvoice(siteId, month, year);

            HttpHeaders headers = new HttpHeaders();
            headers.add(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=invoice_" + year + "_" + month + "_site" + siteId + ".pdf"
            );

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf.readAllBytes());

        } catch (Exception e) {
            throw new RuntimeException("Invoice PDF generation failed: " + e.getMessage());
        }
    }
}
