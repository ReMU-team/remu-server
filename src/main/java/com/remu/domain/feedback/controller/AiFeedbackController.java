package com.remu.domain.feedback.controller;

import com.remu.domain.feedback.dto.response.AiFeedbackResDTO;
import com.remu.domain.feedback.exception.code.AiFeedbackSuccessCode;
import com.remu.domain.feedback.service.AiFeedbackService;
import com.remu.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AiFeedbackController implements AiFeedbackControllerDocs{

    private final AiFeedbackService aiFeedbackService;

    @PostMapping("/galaxies/{galaxyId}/feedback")
    @Override
    public ApiResponse<AiFeedbackResDTO.AiFeedbackCreateDTO> createFeedback(
            @PathVariable Long galaxyId
    ) {
        AiFeedbackResDTO.AiFeedbackCreateDTO result = aiFeedbackService.createFeedback(galaxyId);
        return ApiResponse.onSuccess(AiFeedbackSuccessCode.CREATE, result);
    }

    @GetMapping("/galaxies/{galaxyId}/feedback")
    @Override
    public ApiResponse<AiFeedbackResDTO.AiFeedbackCreateDTO> getFeedback(
            @PathVariable Long galaxyId
    ) {
        AiFeedbackResDTO.AiFeedbackCreateDTO result = aiFeedbackService.readFeedback(galaxyId);
        return ApiResponse.onSuccess(AiFeedbackSuccessCode.FOUND, result);
    }

    @PatchMapping("/galaxies/{galaxyId}/feedback")
    @Override
    public ApiResponse<AiFeedbackResDTO.AiFeedbackUpdateDTO> updateFeedback(
            @PathVariable Long galaxyId
    ) {
        AiFeedbackResDTO.AiFeedbackUpdateDTO result = aiFeedbackService.updateFeedback(galaxyId);
        return ApiResponse.onSuccess(AiFeedbackSuccessCode.UPDATE, result);
    }

}
