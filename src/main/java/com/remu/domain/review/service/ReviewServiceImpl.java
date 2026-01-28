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
    public ReviewResDTO.ReviewCreateDTO create(
            Long userId,
            Long resolutionId,
            ReviewReqDTO.ReviewCreateDTO dto
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

    // 회고 배치 생성
    @Override
    public ReviewResDTO.ReviewBatchCreateDTO batchCreate(
            Long userId,
            Long galaxyId,
            ReviewReqDTO.BatchReviewCreateDTO dto
    ) {
        // 1. 은하 조회

        Galaxy galaxy = galaxyRepository.findById(galaxyId)
                .orElseThrow(() -> new ResolutionException(GeneralErrorCode.NOT_FOUND));

        // 2. 권한 검증(유저 ID 대조)
        if (!galaxy.getUser().getId().equals(userId)) {
            throw new ResolutionException(GeneralErrorCode.FORBIDDEN);
        }

        // 3. 여행 후기 및 이모지 저장
        galaxy.updateReviewEmoji(dto.emojiId());
        galaxy.updateReflection(dto.reflection());

        // 4. 리스트 순회하며 회고 정보 업데이트
        List<Review> reviews = dto.reviews().stream()
                .map(item -> {
                    // 회고 대상 다짐 조회
                    Resolution resolution = resolutionRepository.findById(item.resolutionId())
                            .orElseThrow(() -> new ReviewException(ReviewErrorCode.NOT_FOUND));

                    // 해당 다짐이 이 은하 소속인지 확인
                    if (! resolution.getGalaxy().getId().equals(galaxyId)) {
                        throw new ResolutionException(GeneralErrorCode.FORBIDDEN);
                    }

                    // review 엔티티 생성(Resolution과 1:1 매핑)
                    return ReviewConverter.toReviewFromBatch(resolution, item);
                })
                .toList();

        // 5. 모든 회고 데이터 일괄 저장
        List<Review> savedReviews = reviewRepository.saveAll(reviews);
        return ReviewConverter.toBatchCreateResDTO(galaxy, savedReviews);
    }

    // 리뷰 단일 업데이트

    @Override
    public ReviewResDTO.ReviewUpdateDTO update(
            Long userId,
            Long reviewId,
            ReviewReqDTO.ReviewUpdateDTO dto
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

    // 리뷰 배치 업데이트
    @Override
    public ReviewResDTO.ReviewBatchUpdateDTO batchUpdate(
            Long userId,
            Long galaxyId,
            ReviewReqDTO.BatchReviewUpdateDTO dto
    ) {
        // 1. 은하 조회

        Galaxy galaxy = galaxyRepository.findById(galaxyId)
                .orElseThrow(() -> new ResolutionException(GeneralErrorCode.NOT_FOUND));

        // 2. 권한 검증(유저 ID 대조)
        if (!galaxy.getUser().getId().equals(userId)) {
            throw new ResolutionException(GeneralErrorCode.FORBIDDEN);
        }

        // 3. 여행 후기 및 이모지 저장
        galaxy.updateReviewEmoji(dto.emojiId());
        galaxy.updateReflection(dto.reflection());

        // 4. 리스트 순회하며 회고 정보 업데이트
        List<Review> updatedReviews = dto.reviews().stream()
                .map(item -> {
                    Review review = reviewRepository.findById(item.reviewId())
                            .orElseThrow(() -> new ReviewException(ReviewErrorCode.NOT_FOUND));

                    // 보안 검증: 현재 은하의 회고가 맞는지
                    if (!review.getResolution().getGalaxy().getId().equals(galaxyId)) {
                        throw new ResolutionException(GeneralErrorCode.FORBIDDEN);
                    }

                    // 데이터 업데이트
                    review.update(item.reviewContent(), item.isResolutionFulfilled());

                    return review;
                })
                .toList();

        return ReviewConverter.toBatchUpdateResDTO(galaxy, updatedReviews);
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

        return ReviewConverter.toReviewPreviewListDTO(galaxy ,reviews);

    }
}
