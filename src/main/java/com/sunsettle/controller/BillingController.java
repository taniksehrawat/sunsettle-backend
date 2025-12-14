package com.sunsettle.controller;

import com.sunsettle.dto.BillDTO;
import com.sunsettle.service.BillingService;
import com.sunsettle.service.InvoiceService;
import com.sunsettle.service.TariffService;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/api/billing")
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
     * 1️⃣ SET TARIFF
     *--------------------------------------------------------------*/
    @PostMapping("/set-tariff/{siteId}")
    public String setTariff(@PathVariable Long siteId, @RequestParam Double rate) {
        tariffService.setTariff(siteId, rate);
        return "Tariff set successfully @ ₹" + rate + "/kWh";
    }


    /*--------------------------------------------------------------
     * 2️⃣ GENERATE BILL SUMMARY (JSON)
     *--------------------------------------------------------------*/
    @GetMapping("/generate/{siteId}")
    public BillDTO generateBill(
            @PathVariable Long siteId,
            @RequestParam int month,
            @RequestParam int year) {

        return billingService.generateBill(siteId, month, year);
    }


    /*--------------------------------------------------------------
     * 3️⃣ GENERATE + DOWNLOAD INVOICE PDF
     *--------------------------------------------------------------*/
    @GetMapping("/invoice/pdf/{siteId}")
    public void downloadInvoicePdf(
            @PathVariable Long siteId,
            @RequestParam int month,
            @RequestParam int year,
            HttpServletResponse response
    ) {
        try {
            InputStream pdfStream = invoiceService.generateInvoice(siteId, month, year);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=invoice_" + year + "_" + month + "_site" + siteId + ".pdf");

            pdfStream.transferTo(response.getOutputStream());
            response.flushBuffer();

        } catch (Exception e) {
            throw new RuntimeException("Invoice PDF error: " + e.getMessage());
        }
    }
}
