package com.example.GasTuanDat.report;

import com.example.GasTuanDat.report.dtos.DashboardResponse;
import com.example.GasTuanDat.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        OffsetDateTime start = startDate != null ? startDate.atStartOfDay().atOffset(ZoneOffset.ofHours(7)) : OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.ofHours(7));
        OffsetDateTime end = endDate != null ? endDate.atTime(23, 59, 59).atOffset(ZoneOffset.ofHours(7)) : OffsetDateTime.of(2100, 1, 1, 0, 0, 0, 0, ZoneOffset.ofHours(7));

        DashboardResponse response = dashboardService.getDashboardData(start, end);

        return ResponseEntity.ok(ApiResponse.<DashboardResponse>builder()
                .code(200)
                .message("Success")
                .data(response)
                .build());
    }
}
