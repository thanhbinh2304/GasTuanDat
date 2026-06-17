package com.example.GasTuanDat.gasBook;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

import com.example.GasTuanDat.common.response.ApiResponse;
import com.example.GasTuanDat.gasBook.dtos.RewardMilestoneCreateRequest;
import com.example.GasTuanDat.gasBook.dtos.RewardMilestoneResponse;
import com.example.GasTuanDat.gasBook.dtos.RewardMilestoneUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/reward-milestones")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "RewardMilestone", description = "Reward milestone management APIs")
public class RewardMilestoneController {
    RewardMilestoneService rewardMilestoneService;

    @PostMapping
    @Operation(summary = "Create reward milestone")
    public ApiResponse<RewardMilestoneResponse> create(@Valid @RequestBody RewardMilestoneCreateRequest request) {
        return ApiResponse.<RewardMilestoneResponse>builder()
                .success(true)
                .code(200)
                .message("Create reward milestone success")
                .data(rewardMilestoneService.create(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Search reward milestones")
    public ApiResponse<Page<RewardMilestoneResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return ApiResponse.<Page<RewardMilestoneResponse>>builder()
                .success(true)
                .code(200)
                .message("Get reward milestones success")
                .data(rewardMilestoneService.search(keyword, startDate, endDate, PageRequest.of(page - 1, limit)))
                .build();
    }

    @GetMapping("/{milestoneId}")
    @Operation(summary = "Get reward milestone detail by id")
    public ApiResponse<RewardMilestoneResponse> getById(@PathVariable UUID milestoneId) {
        return ApiResponse.<RewardMilestoneResponse>builder()
                .success(true)
                .code(200)
                .message("Get reward milestone success")
                .data(rewardMilestoneService.getById(milestoneId))
                .build();
    }

    @PutMapping("/{milestoneId}")
    @Operation(summary = "Update reward milestone")
    public ApiResponse<RewardMilestoneResponse> update(@PathVariable UUID milestoneId,
            @Valid @RequestBody RewardMilestoneUpdateRequest request) {
        return ApiResponse.<RewardMilestoneResponse>builder()
                .success(true)
                .code(200)
                .message("Update reward milestone success")
                .data(rewardMilestoneService.update(milestoneId, request))
                .build();
    }

    @DeleteMapping("/{milestoneId}")
    @Operation(summary = "Delete reward milestone")
    public ApiResponse<Void> delete(@PathVariable UUID milestoneId) {
        rewardMilestoneService.delete(milestoneId);
        return ApiResponse.<Void>builder()
                .success(true)
                .code(200)
                .message("Delete reward milestone success")
                .build();
    }
}
