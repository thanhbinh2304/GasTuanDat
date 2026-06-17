package com.example.GasTuanDat.gasBook.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardMilestoneUpdateRequest {
    private String promotionCode;

    @NotBlank
    private String promotionName;

    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull
    private BigDecimal leastValue;

    @NotNull
    private BigDecimal value;

    private Integer rewardQuantity;
    private String rewardName;
    private Double percentage;
    private String notes;
    private List<PromotionDetailCreateRequest> promotionDetails;
}
