package com.remu.domain.review.service;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.galaxy.exception.GalaxyException;
import com.remu.domain.galaxy.repository.GalaxyRepository;
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
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final ResolutionRepository resolutionRepository;
    private final GalaxyRepository galaxyRepository;

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

    @Override
    public ReviewResDTO.UpdateDTO update(
            Long userId,
            Long reviewId,
            ReviewReqDTO.UpdateDTO dto
    ) {
        // 1. 기존 회고 조회
        // TODO: 향후 fetch 조인 적용 고려
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.NOT_FOUND));

        // 2. 권한 검증
        if (!review.getResolution().getGalaxy().getUser().getId().equals(userId)) {
            throw new ReviewException(GeneralErrorCode.FORBIDDEN);
        }

        // 3. 수정(엔티티 메서드 호출)
        review.update(dto.content(), dto.isResolutionFulfilled());

        return ReviewConverter.toUpdateDTO(review);
    }

    // === Query 로직 (조회) ===
    @Override
    @Transactional(readOnly = true)
    public ReviewResDTO.ReviewPreviewListDTO getReviewListByGalaxy(
            Long userId,
            Long galaxyId
    ) {
        // 1. 은하 존재 여부 확인
        // TODO: 향후 에러 코드 수정 필요
        Galaxy galaxy = galaxyRepository.findById(galaxyId)
                .orElseThrow(() -> new GalaxyException(GeneralErrorCode.NOT_FOUND));

        // 2. 본인 확인
        if (!galaxy.getUser().getId().equals(userId)) {
            throw new ReviewException(GeneralErrorCode.FORBIDDEN);
        }

        // 3. 데이터 조회(N+1 문제 방지 코드)
        List<Review> reviews = reviewRepository.findAllByGalaxyId(galaxyId);

        return ReviewConverter.toReviewPreviewListDTO(reviews);

    }
}
