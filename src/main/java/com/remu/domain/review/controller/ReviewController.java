package com.remu.domain.review.controller;

import com.remu.domain.review.dto.ReviewReqDTO;
import com.remu.domain.review.dto.ReviewResDTO;
import com.remu.domain.review.exception.code.ReviewSuccessCode;
import com.remu.domain.review.service.ReviewService;
import com.remu.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController implements ReviewControllerDocs {

    private final ReviewService reviewService;

    @Override
    @PostMapping("/resolutions/{resolutionId}/reviews")
    public ApiResponse<ReviewResDTO.CreateDTO> createReview(
            @RequestParam Long userId,
            @PathVariable Long resolutionId,
            @Valid @RequestBody ReviewReqDTO.CreateDTO dto
    ) {
        ReviewResDTO.CreateDTO result = reviewService.create(userId, resolutionId, dto);
        return ApiResponse.onSuccess(ReviewSuccessCode.CREATE, result);
    }

    // 리뷰 목록 조회. 이때 은하 내부 모든 회고를 갖고 와야 하기에 galaxy로 접근
    @Override
    @GetMapping("/galaxies/{galaxyId}/reviews")
    public ApiResponse<ReviewResDTO.ReviewPreviewListDTO> getReviews(
            @RequestParam Long userId,
            @PathVariable Long galaxyId
    ) {
        ReviewResDTO.ReviewPreviewListDTO result = reviewService.getReviewListByGalaxy(userId, galaxyId);
        return ApiResponse.onSuccess(ReviewSuccessCode.FOUND, result);
    }
}
