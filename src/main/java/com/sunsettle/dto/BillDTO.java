package com.sunsettle.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDTO {

    private Long siteId;
    private String siteName;

    private String month;       // e.g. "Jan-2025"
    private Integer monthNumber; 
    private Integer year;

    private Double exportKwh;
    private Double tariff;
    private Double energyCharge;
    private Double gst18;
    private Double totalPayable;
}
