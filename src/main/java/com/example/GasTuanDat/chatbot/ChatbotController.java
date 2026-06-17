package com.example.GasTuanDat.chatbot;

import static com.example.GasTuanDat.common.constants.RoleConstants.ADMIN;
import static com.example.GasTuanDat.common.constants.RoleConstants.SALES;
import static com.example.GasTuanDat.common.constants.RoleConstants.WAREHOUSE;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GasTuanDat.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
@Tag(name = "Chatbot", description = "AI Chatbot API")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/chat")
    @PreAuthorize("hasAnyRole('" + ADMIN + "', '" + SALES + "', '" + WAREHOUSE + "')")
    @Operation(summary = "Chat with AI Assistant (ADMIN, SALES, WAREHOUSE)")
    public ApiResponse<ChatbotResponse> chat(@RequestBody ChatbotRequest request) {
        return ApiResponse.<ChatbotResponse>builder()
                .code(200)
                .message("Success")
                .data(chatbotService.chat(request))
                .build();
    }
}
