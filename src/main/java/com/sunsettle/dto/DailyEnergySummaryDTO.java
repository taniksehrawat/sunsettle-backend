package com.sunsettle.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyEnergySummaryDTO {

    private String date;
    private Double generationKwh;
    private Double exportKwh;
    private Double importKwh;
    private Double netGenerationKwh;  // generation - import
    private Double cuf;               // Capacity Utilization Factor %
}
