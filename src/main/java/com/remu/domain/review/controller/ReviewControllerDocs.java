package com.remu.domain.review.controller;

import com.remu.domain.review.dto.ReviewReqDTO;
import com.remu.domain.review.dto.ReviewResDTO;
import com.remu.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReviewControllerDocs {

    @Operation(
            summary = "리뷰를 생성하는 API by 매튜/진현준",
            description = "리뷰를 생성하는 API입니다. 생성 과정에 userId, resolutionId를 통해 유효한 사용자인지 검증합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<ReviewResDTO.CreateDTO> createReview(
            @RequestParam Long userId,
            @PathVariable Long resolutionId,
            @Valid @RequestBody ReviewReqDTO.CreateDTO dto
    );
}
