package com.example.GasTuanDat.gasBook.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardMilestoneResponse {
    private UUID promotionId;
    private String promotionCode;
    private String promotionName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal leastValue;
    private BigDecimal value;
    private Integer rewardQuantity;
    private String rewardName;
    private Double percentage;
    private String notes;
    private java.util.List<PromotionDetailResponse> promotionDetails;
}
