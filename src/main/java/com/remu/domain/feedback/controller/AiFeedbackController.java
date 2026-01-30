package com.remu.domain.feedback.controller;

import com.remu.domain.feedback.dto.response.AiFeedbackResDTO;
import com.remu.domain.feedback.exception.code.AiFeedbackSuccessCode;
import com.remu.domain.feedback.service.AiFeedbackService;
import com.remu.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
