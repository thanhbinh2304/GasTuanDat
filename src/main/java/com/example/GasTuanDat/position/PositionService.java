package com.example.GasTuanDat.position;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.position.dtos.PositionCreationRequest;
import com.example.GasTuanDat.position.dtos.PositionUpdateRequest;
import com.example.GasTuanDat.position.entities.PositionEntity;
import com.example.GasTuanDat.position.mapper.PositionMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PositionService {
    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    public PositionEntity create(PositionCreationRequest request) {
        positionRepository.findByName(request.getPositionName())
                .ifPresent(position -> {
                    throw new ApiException(ErrorCode.INVALID_INPUT);
                });

        PositionEntity position = positionMapper.toEntity(request);
        return positionRepository.save(position);
    }

    public List<PositionEntity> getAll() {
        return positionRepository.findAll();
    }

    public PositionEntity getById(UUID positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new ApiException(ErrorCode.POSITION_NOT_FOUND));
    }

    public PositionEntity update(UUID positionId, PositionUpdateRequest request) {
        PositionEntity position = getById(positionId);
        positionMapper.updateEntity(request, position);
        return positionRepository.save(position);
    }

    public void delete(UUID positionId) {
        PositionEntity position = getById(positionId);
        positionRepository.delete(position);
    }
}