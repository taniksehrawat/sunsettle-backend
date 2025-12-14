package com.sunsettle.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyEnergySummaryDTO {

    private Long siteId;
    private String month;

    private Double totalGeneration;
    private Double totalExport;
    private Double totalImport;

    private Double netGeneration;
    private Double averageCuf;   // replaces cuf
}
