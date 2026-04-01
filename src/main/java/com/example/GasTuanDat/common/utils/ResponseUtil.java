package com.example.GasTuanDat.common.utils;

import com.example.GasTuanDat.common.response.ApiResponse;

public class ResponseUtil {
    
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message("Success")
                .data(data)
                .build();
    }
}
