package com.remu.domain.review.controller;

import com.remu.domain.resolution.repository.ResolutionRepository;
import com.remu.domain.review.dto.ReviewReqDTO;
import com.remu.domain.review.dto.ReviewResDTO;
import com.remu.domain.review.exception.code.ReviewSuccessCode;
import com.remu.domain.review.service.ReviewService;
import com.remu.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/galaxies/{galaxyId}/reviews")
public class ReviewController implements ReviewControllerDocs {

    private final ReviewService reviewService;

    @PostMapping
    @Override
    public ApiResponse<ReviewResDTO.ReviewBatchCreateDTO> createReviewBatch(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable Long galaxyId,
            @Valid @RequestBody ReviewReqDTO.BatchReviewCreateDTO dto
    ) {
        ReviewResDTO.ReviewBatchCreateDTO result = reviewService.batchCreate(userId, galaxyId, dto);
        return ApiResponse.onSuccess(ReviewSuccessCode.CREATE, result);
    }

    // 리뷰 목록 조회. 이때 은하 내부 모든 회고를 갖고 와야 하기에 galaxy로 접근
    @Override
    @GetMapping
    public ApiResponse<ReviewResDTO.ReviewPreviewListDTO> getReviews(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable Long galaxyId
    ) {
        ReviewResDTO.ReviewPreviewListDTO result = reviewService.getReviewListByGalaxy(userId, galaxyId);
        return ApiResponse.onSuccess(ReviewSuccessCode.FOUND, result);
    }

    @PatchMapping
    @Override
    public ApiResponse<ReviewResDTO.ReviewBatchUpdateDTO> updateReviewBatch(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable Long galaxyId,
            @Valid @RequestBody ReviewReqDTO.BatchReviewUpdateDTO dto
    ) {
        ReviewResDTO.ReviewBatchUpdateDTO result = reviewService.batchUpdate(userId, galaxyId, dto);

        return ApiResponse.onSuccess(ReviewSuccessCode.UPDATE, result);
    }
}
