package com.remu.domain.star.service;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.galaxy.repository.GalaxyRepository;
import com.remu.domain.star.converter.StarConverter;
import com.remu.domain.star.dto.request.StarCreateRequest;
import com.remu.domain.star.dto.request.StarUpdateRequest;
import com.remu.domain.star.dto.response.StarResponseDto;
import com.remu.domain.star.entity.Star;
import com.remu.domain.star.exception.StarException;
import com.remu.domain.star.exception.code.StarErrorCode;
import com.remu.domain.star.repository.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StarService {

    private final StarRepository starRepository;
    private final GalaxyRepository galaxyRepository;
    // private final S3Service s3Service; // 나중에 추가될 S3 업로드 서비스

    @Transactional
    public Long createStar(StarCreateRequest request) {
        // 1. 은하 조회
        Galaxy galaxy = galaxyRepository.findById(request.getGalaxyId())
                .orElseThrow(() -> new StarException(StarErrorCode.GALAXY_NOT_FOUND));

        // 2. 이미지 업로드
        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            // imageUrl = s3Service.upload(request.getImage());
            imageUrl = "https://dummy-s3-url.com/image.jpg"; // 임시
        }

        // 3. Star 엔티티 생성 및 저장 (Converter 사용)
        Star star = StarConverter.toStar(request, galaxy, imageUrl);
        
        Star savedStar = starRepository.save(star); // 저장된 객체 반환받기

        return savedStar.getId(); // 저장된 객체의 ID 반환
    }

    // 별 수정
    @Transactional
    public Long updateStar(Long starId, StarUpdateRequest request) {
        // 1. 별 조회
        Star star = starRepository.findById(starId)
                .orElseThrow(() -> new StarException(StarErrorCode.STAR_NOT_FOUND));

        // 2. 기본 정보 수정
        star.update(
                request.getTitle(),
                request.getContent(),
                request.getRecordDate(),
                request.getCardColor(),
                request.getEmojis()
        );

        // 3. 이미지 처리 로직
        // Case 1: 새 이미지가 있는 경우 -> 업로드 후 교체
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            // String newImageUrl = s3Service.upload(request.getImage());
            String newImageUrl = "https://dummy-s3-url.com/updated_image.jpg"; // 임시
            star.updateImageUrl(newImageUrl);
        }
        // Case 2: 이미지는 없는데 삭제 요청이 있는 경우 -> 삭제 (null 처리)
        else if (Boolean.TRUE.equals(request.getIsImageDeleted())) {
            // s3Service.delete(star.getImageUrl()); // 기존 S3 파일 삭제 로직 필요 시 추가
            star.updateImageUrl(null);
        }
        // Case 3: 둘 다 아니면 기존 이미지 유지 (아무것도 안 함)

        return star.getId();
    }

    // 별 삭제
    @Transactional
    public void deleteStar(Long starId) {
        // 1. 별 조회
        Star star = starRepository.findById(starId)
                .orElseThrow(() -> new StarException(StarErrorCode.STAR_NOT_FOUND));

        // 2. 이미지 삭제 (S3)
        if (star.getImageUrl() != null) {
            // s3Service.delete(star.getImageUrl());
        }

        // 3. DB 삭제
        starRepository.delete(star);
    }

    // 은하별 별 목록 조회
    public List<StarResponseDto.StarPreview> getStarsByGalaxyId(Long galaxyId) {
        // 1. 은하 존재 여부 확인
        Galaxy galaxy = galaxyRepository.findById(galaxyId)
                .orElseThrow(() -> new StarException(StarErrorCode.GALAXY_NOT_FOUND));

        // 2. 별 목록 조회
        List<Star> stars = starRepository.findAllByGalaxyId(galaxyId);

        // 3. DTO 변환 (Converter 사용)
        return stars.stream()
                .map(star -> {
                    int dDay = (int) ChronoUnit.DAYS.between(galaxy.getStartDate(), star.getRecordDate()) + 1;
                    return StarConverter.toStarPreview(star, dDay);
                })
                .collect(Collectors.toList());
    }

    // 별 상세 조회
    public StarResponseDto.StarDetail getStarDetail(Long starId) {
        // 1. 별 조회 (Fetch Join으로 Galaxy 함께 조회)
        Star star = starRepository.findByIdFetchJoin(starId)
                .orElseThrow(() -> new StarException(StarErrorCode.STAR_NOT_FOUND));

        // 2. DTO 변환 (Converter 사용)
        int dDay = (int) ChronoUnit.DAYS.between(star.getGalaxy().getStartDate(), star.getRecordDate()) + 1;
        return StarConverter.toStarDetail(star, dDay);
    }
}