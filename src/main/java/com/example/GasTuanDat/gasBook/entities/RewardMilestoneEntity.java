package com.example.GasTuanDat.gasBook.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"RewardMilestone\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardMilestoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"promotionId\"", nullable = false)
    private UUID promotionId;

    @Column(name = "\"promotionCode\"", unique = true)
    private String promotionCode;

    @Column(name = "\"notes\"")
    private String notes;

    @Column(name = "\"rewardQuantity\"")
    private Integer rewardQuantity;

    @Column(name = "\"rewardName\"")
    private String rewardName;

    @Column(name = "\"promotionName\"", nullable = false)
    private String promotionName;   

    @Column(name = "\"startDate\"", nullable = false)
    private LocalDate startDate;

    @Column(name = "\"endDate\"", nullable = false)
    private LocalDate endDate;

    @Column(name = "\"leastValue\"", nullable = false)
    private BigDecimal leastValue;

    @Column(name = "\"value\"", nullable = false)
    private BigDecimal value;

    @Column(name = "\"percentage\"")
    private Double percentage;

    @OneToMany(mappedBy = "rewardMilestone", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PromotionDetailEntity> promotionDetails = new ArrayList<>();
}
