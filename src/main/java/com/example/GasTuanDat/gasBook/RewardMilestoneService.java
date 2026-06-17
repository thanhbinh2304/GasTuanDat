package com.example.GasTuanDat.gasBook;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.gasBook.dtos.PromotionDetailCreateRequest;
import com.example.GasTuanDat.gasBook.dtos.PromotionDetailResponse;
import com.example.GasTuanDat.gasBook.dtos.RewardMilestoneCreateRequest;
import com.example.GasTuanDat.gasBook.dtos.RewardMilestoneResponse;
import com.example.GasTuanDat.gasBook.dtos.RewardMilestoneUpdateRequest;
import com.example.GasTuanDat.gasBook.entities.PromotionDetailEntity;
import com.example.GasTuanDat.gasBook.entities.RewardMilestoneEntity;
import com.example.GasTuanDat.gasBook.mapper.RewardMilestoneMapper;
import com.example.GasTuanDat.product.ProductRepository;
import com.example.GasTuanDat.product.entities.ProductEntity;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RewardMilestoneService {
    private final RewardMilestoneRepository rewardMilestoneRepository;
    private final RewardMilestoneMapper rewardMilestoneMapper;
    private final ProductRepository productRepository;

    public RewardMilestoneResponse create(RewardMilestoneCreateRequest request) {
        if (request.getPromotionCode() == null || request.getPromotionCode().trim().isEmpty()) {
            request.setPromotionCode(generatePromotionCode());
        }
        RewardMilestoneEntity entity = rewardMilestoneMapper.toEntity(request);
        entity.setPromotionDetails(new ArrayList<>());

        // Lưu hàng tặng vào PromotionDetail
        if (request.getPromotionDetails() != null) {
            for (PromotionDetailCreateRequest detail : request.getPromotionDetails()) {
                if (detail.getProductId() == null) continue;
                ProductEntity product = productRepository.findById(UUID.fromString(detail.getProductId()))
                        .orElse(null);
                if (product == null) continue;
                PromotionDetailEntity detailEntity = PromotionDetailEntity.builder()
                        .rewardMilestone(entity)
                        .product(product)
                        .quantity(detail.getQuantity() != null ? detail.getQuantity() : 1)
                        .build();
                entity.getPromotionDetails().add(detailEntity);
            }
        }

        RewardMilestoneEntity saved = rewardMilestoneRepository.save(entity);
        return toResponseWithDetails(saved);
    }

    private String generatePromotionCode() {
        String code;
        do {
            int randomNum = new Random().nextInt(90000) + 10000;
            code = "RM" + randomNum;
        } while (rewardMilestoneRepository.existsByPromotionCode(code));
        return code;
    }

    public RewardMilestoneResponse getById(UUID milestoneId) {
        RewardMilestoneEntity entity = rewardMilestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ApiException(ErrorCode.REWARD_MILESTONE_NOT_FOUND));
        return toResponseWithDetails(entity);
    }

    public Page<RewardMilestoneResponse> search(String keyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<RewardMilestoneEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.trim().isEmpty()) {
                String pattern = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("promotionCode")), pattern),
                    cb.like(cb.lower(root.get("promotionName")), pattern)
                ));
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), endDate));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return rewardMilestoneRepository.findAll(spec, pageable).map(this::toResponseWithDetails);
    }

    public RewardMilestoneResponse update(UUID milestoneId, RewardMilestoneUpdateRequest request) {
        RewardMilestoneEntity entity = rewardMilestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ApiException(ErrorCode.REWARD_MILESTONE_NOT_FOUND));

        rewardMilestoneMapper.updateEntity(request, entity);

        // Cập nhật lại danh sách hàng tặng
        entity.getPromotionDetails().clear();
        if (request.getPromotionDetails() != null) {
            for (PromotionDetailCreateRequest detail : request.getPromotionDetails()) {
                if (detail.getProductId() == null) continue;
                ProductEntity product = productRepository.findById(UUID.fromString(detail.getProductId()))
                        .orElse(null);
                if (product == null) continue;
                PromotionDetailEntity detailEntity = PromotionDetailEntity.builder()
                        .rewardMilestone(entity)
                        .product(product)
                        .quantity(detail.getQuantity() != null ? detail.getQuantity() : 1)
                        .build();
                entity.getPromotionDetails().add(detailEntity);
            }
        }

        return toResponseWithDetails(rewardMilestoneRepository.save(entity));
    }

    public void delete(UUID milestoneId) {
        RewardMilestoneEntity entity = rewardMilestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ApiException(ErrorCode.REWARD_MILESTONE_NOT_FOUND));
        rewardMilestoneRepository.delete(entity);
    }

    private RewardMilestoneResponse toResponseWithDetails(RewardMilestoneEntity entity) {
        RewardMilestoneResponse response = rewardMilestoneMapper.toResponse(entity);
        List<PromotionDetailResponse> details = entity.getPromotionDetails().stream()
                .map(d -> PromotionDetailResponse.builder()
                        .id(d.getId())
                        .productId(d.getProduct() != null ? d.getProduct().getProductId().toString() : null)
                        .productName(d.getProduct() != null ? d.getProduct().getProductName() : null)
                        .quantity(d.getQuantity())
                        .build())
                .toList();
        response.setPromotionDetails(details);
        return response;
    }
}
