package com.example.GasTuanDat.gasBook.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.gasBook.dtos.RewardMilestoneCreateRequest;
import com.example.GasTuanDat.gasBook.dtos.RewardMilestoneResponse;
import com.example.GasTuanDat.gasBook.dtos.RewardMilestoneUpdateRequest;
import com.example.GasTuanDat.gasBook.entities.RewardMilestoneEntity;

@Mapper(componentModel = "spring")
public interface RewardMilestoneMapper {

    @Mapping(target = "promotionId", ignore = true)
    @Mapping(target = "promotionDetails", ignore = true)
    RewardMilestoneEntity toEntity(RewardMilestoneCreateRequest request);

    @Mapping(target = "promotionDetails", ignore = true)
    RewardMilestoneResponse toResponse(RewardMilestoneEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "promotionCode", source = "promotionCode")
    @Mapping(target = "promotionName", source = "promotionName")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "leastValue", source = "leastValue")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "rewardQuantity", source = "rewardQuantity")
    @Mapping(target = "rewardName", source = "rewardName")
    @Mapping(target = "percentage", source = "percentage")
    @Mapping(target = "notes", source = "notes")
    void updateEntity(RewardMilestoneUpdateRequest request, @MappingTarget RewardMilestoneEntity entity);
}
