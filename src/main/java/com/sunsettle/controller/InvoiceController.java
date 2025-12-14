package com.sunsettle.controller;

import com.sunsettle.service.InvoiceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // =============================================
    //  CLIENT & ADMIN COMMON DOWNLOAD API
    // =============================================
    @GetMapping("/api/invoice/download/{siteId}")
    public ResponseEntity<byte[]> downloadInvoice(
            @PathVariable Long siteId,
            @RequestParam int month,
            @RequestParam int year
    ) throws Exception {

        byte[] pdf = invoiceService.generateInvoice(siteId, month, year).readAllBytes();

        String filename = "invoice_" + year + "_" + month + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // =============================================
    //  ADMIN PDF ENDPOINT FOR FRONTEND
    //  Matches: /admin/invoice/pdf/{siteId}
    // =============================================
    @GetMapping("/admin/invoice/pdf/{siteId}")
    public ResponseEntity<byte[]> adminDownloadInvoice(
            @PathVariable Long siteId,
            @RequestParam int month,
            @RequestParam int year
    ) throws Exception {

        byte[] pdf = invoiceService.generateInvoice(siteId, month, year).readAllBytes();

        String filename = "invoice_" + year + "_" + month + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
