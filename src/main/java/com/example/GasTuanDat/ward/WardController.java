package com.example.GasTuanDat.ward;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.GasTuanDat.common.response.ApiResponse;
import com.example.GasTuanDat.ward.entities.AreaEntity;
import com.example.GasTuanDat.ward.entities.WardEntity;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/wards")
@RequiredArgsConstructor
public class WardController {
    private final WardRepository wardRepository;
    private final AreaRepository areaRepository;

    @GetMapping
    public ApiResponse<List<WardEntity>> getAllWards() {
        return ApiResponse.<List<WardEntity>>builder()
                .success(true)
                .code(200)
                .message("Get wards success")
                .data(wardRepository.findAll())
                .build();
    }

    @GetMapping("/areas")
    public ApiResponse<List<AreaEntity>> getAllAreas() {
        return ApiResponse.<List<AreaEntity>>builder()
                .success(true)
                .code(200)
                .message("Get areas success")
                .data(areaRepository.findAll())
                .build();
    }
}
