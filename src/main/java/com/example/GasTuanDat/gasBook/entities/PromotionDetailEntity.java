package com.example.GasTuanDat.gasBook.entities;

import java.util.UUID;

import com.example.GasTuanDat.product.entities.ProductEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"PromotionDetail\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "\"rewardMilestoneId\"")
    private RewardMilestoneEntity rewardMilestone;

    @ManyToOne
    @JoinColumn(name = "\"productId\"")
    private ProductEntity product;

    @Column(name = "quantity")
    private Integer quantity;
}
