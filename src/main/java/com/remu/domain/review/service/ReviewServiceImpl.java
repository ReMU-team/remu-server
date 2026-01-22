package com.remu.domain.review.service;

import com.remu.domain.resolution.entity.Resolution;
import com.remu.domain.resolution.exception.ResolutionException;
import com.remu.domain.resolution.exception.code.ResolutionErrorCode;
import com.remu.domain.resolution.repository.ResolutionRepository;
import com.remu.domain.review.converter.ReviewConverter;
import com.remu.domain.review.dto.ReviewReqDTO;
import com.remu.domain.review.dto.ReviewResDTO;
import com.remu.domain.review.entity.Review;
import com.remu.domain.review.exception.ReviewException;
import com.remu.domain.review.exception.code.ReviewErrorCode;
import com.remu.domain.review.repository.ReviewRepository;
import com.remu.global.apiPayload.code.GeneralErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final ResolutionRepository resolutionRepository;

    // === Command 로직 (상태 변경) ===

    @Override
    public ReviewResDTO.CreateDTO create(
            Long userId,
            Long resolutionId,
            ReviewReqDTO.CreateDTO dto
    ) {

        // 1. 대상 다짐 조회
        Resolution resolution = resolutionRepository.findById(resolutionId)
                .orElseThrow(() -> new ResolutionException(ResolutionErrorCode.NOT_FOUND));

        // 2. 본인 확인
        if (!resolution.getGalaxy().getUser().getId().equals(userId)) {
            throw new ReviewException(GeneralErrorCode.FORBIDDEN);
        }

        // 3. 여행 종료 확인
        if (LocalDate.now().isBefore(resolution.getGalaxy().getEndDate())) {
            throw new ReviewException(ReviewErrorCode.TRAVEL_NOT_FINISHED);
        }

        // 4. 중복 작성 확인: 이미 해당 다짐에 대한 회고가 있는지
        if (reviewRepository.existsByResolutionId(resolutionId)) {
            throw new ReviewException(ReviewErrorCode.ALREADY_EXISTS);
        }

        // DTO -> Entity 변환
        Review review = ReviewConverter.toReview(dto, resolution);

        // 이후 저장
        return ReviewConverter.toCreateDTO(reviewRepository.save(review));
    }
}
