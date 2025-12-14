package com.sunsettle.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long siteId;
    private String siteName;

    private String month;        // "Jan-2025"
    private Integer monthNumber; // 1
    private Integer year;

    private Double exportKwh;
    private Double energyCharge;

    private Double gst18;
    private Double totalPayable;

    private String pdfFileName;
    private LocalDateTime createdAt;

    private Long clientId; 
          // required for bill filtering

}
