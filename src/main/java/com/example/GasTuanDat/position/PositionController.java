package com.example.GasTuanDat.position;

import com.example.GasTuanDat.common.response.ApiResponse;
import com.example.GasTuanDat.position.dtos.PositionCreationRequest;
import com.example.GasTuanDat.position.dtos.PositionResponse;
import com.example.GasTuanDat.position.dtos.PositionUpdateRequest;
import com.example.GasTuanDat.position.mapper.PositionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Position", description = "Position management APIs")
public class PositionController {
    PositionService positionService;
    PositionMapper positionMapper;

    @PostMapping
    @Operation(summary = "Create position")
    public ApiResponse<PositionResponse> create(@Valid @RequestBody PositionCreationRequest request) {
        return ApiResponse.<PositionResponse>builder()
                .code(200)
                .message("Create position success")
                .data(positionMapper.toResponse(positionService.create(request)))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all positions")
    public ApiResponse<List<PositionResponse>> getAll() {
        return ApiResponse.<List<PositionResponse>>builder()
                .code(200)
                .message("Get positions success")
                .data(positionService.getAll().stream().map(positionMapper::toResponse).toList())
                .build();
    }

    @GetMapping("/{positionId}")
    @Operation(summary = "Get position by id")
    public ApiResponse<PositionResponse> getById(@PathVariable UUID positionId) {
        return ApiResponse.<PositionResponse>builder()
                .code(200)
                .message("Get position success")
                .data(positionMapper.toResponse(positionService.getById(positionId)))
                .build();
    }

    @PutMapping("/{positionId}")
    @Operation(summary = "Update position")
    public ApiResponse<PositionResponse> update(@PathVariable UUID positionId,
            @Valid @RequestBody PositionUpdateRequest request) {
        return ApiResponse.<PositionResponse>builder()
                .code(200)
                .message("Update position success")
                .data(positionMapper.toResponse(positionService.update(positionId, request)))
                .build();
    }

    @DeleteMapping("/{positionId}")
    @Operation(summary = "Delete position")
    public ApiResponse<Void> delete(@PathVariable UUID positionId) {
        positionService.delete(positionId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete position success")
                .build();
    }
}