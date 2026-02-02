package com.remu.domain.feedback.controller;

import com.remu.domain.feedback.dto.response.AiFeedbackResDTO;
import com.remu.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;

public interface AiFeedbackControllerDocs {

    @Operation(
            summary = "AI 피드백 생성하는 API by 매튜/진현준",
            description = "AI 피드백 생성 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<AiFeedbackResDTO.AiFeedbackCreateDTO> createFeedback(
            @PathVariable Long galaxyId
    );

    @Operation(
            summary = "AI 피드백 조회 API by 매튜/진현준",
            description = "AI 피드백 조회 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<AiFeedbackResDTO.AiFeedbackCreateDTO> getFeedback(
            @PathVariable Long galaxyId
    );

    @Operation(
            summary = "AI 피드백 수정 API by 매튜/진현준",
            description = "AI 피드백 수정 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<AiFeedbackResDTO.AiFeedbackUpdateDTO> updateFeedback(
            @PathVariable Long galaxyId
    );
}
