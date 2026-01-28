package com.remu.domain.review.controller;

import com.remu.domain.review.dto.ReviewReqDTO;
import com.remu.domain.review.dto.ReviewResDTO;
import com.remu.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

public interface ReviewControllerDocs {

    @Operation(
            summary = "리뷰를 생성하는 API by 매튜/진현준",
            description = "리뷰를 생성하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<ReviewResDTO.ReviewCreateDTO> createReview(
            @RequestParam Long userId,
            @PathVariable Long resolutionId,
            @Valid @RequestBody ReviewReqDTO.CreateDTO dto
    );

    @Operation(
            summary = "리뷰 배치 생성 API by 매튜/진현준",
            description = "리뷰 배치 생성 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<ReviewResDTO.ReviewBatchCreateDTO> createReviewBatch(
            @RequestParam Long userId,
            @PathVariable Long galaxyId,
            @Valid @RequestBody ReviewReqDTO.BatchReviewCreateDTO dto
    );

    @Operation(
            summary = "리뷰를 조회하는 API by 매튜/진현준",
            description = "리뷰를 조회하는 API입니다. "
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<ReviewResDTO.ReviewPreviewListDTO> getReviews(
            @RequestParam Long userId,
            @PathVariable Long galaxyId
    );

    // 리뷰 수정
    @Operation(
            summary = "리뷰를 수정하는 API by 매튜/진현준",
            description = "리뷰를 수정하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<ReviewResDTO.UpdateDTO> updateReview(
            @RequestParam Long userId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewReqDTO.UpdateDTO dto
    );
}
