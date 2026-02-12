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
import com.remu.global.s3.S3Directory;
import com.remu.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StarService {

    private final StarRepository starRepository;
    private final GalaxyRepository galaxyRepository;
    private final S3Service s3Service;

    @Transactional
    public Long createStar(StarCreateRequest request, MultipartFile image) {
        // 1. 은하 조회
        Galaxy galaxy = galaxyRepository.findById(request.getGalaxyId())
                .orElseThrow(() -> new StarException(StarErrorCode.GALAXY_NOT_FOUND));

        // 2. 이미지 업로드
        String fileName = null;
        if (image != null && !image.isEmpty()) {
            // S3 업로드 후 파일명 반환
            S3Service.S3TotalResponse response = s3Service.uploadFile(image, S3Directory.STAR, galaxy.getUser().getId());
            fileName = response.fileName();
        }

        // 3. Star 엔티티 생성 및 저장 (Converter 사용)
        Star star = StarConverter.toStar(request, galaxy, fileName);
        
        Star savedStar = starRepository.save(star);

        return savedStar.getId();
    }

    // 별 수정
    @Transactional
    public Long updateStar(Long starId, StarUpdateRequest request, MultipartFile image) {
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
        if (image != null && !image.isEmpty()) {
            // 기존 이미지 삭제
            if (star.getImageUrl() != null) {
                s3Service.deleteFile(star.getImageUrl());
            }
            // 새 이미지 업로드
            S3Service.S3TotalResponse response = s3Service.uploadFile(image, S3Directory.STAR, star.getGalaxy().getUser().getId());
            star.updateImageUrl(response.fileName());
        }
        // Case 2: 이미지는 없는데 삭제 요청이 있는 경우 -> 삭제 (null 처리)
        else if (Boolean.TRUE.equals(request.getIsImageDeleted())) {
            if (star.getImageUrl() != null) {
                s3Service.deleteFile(star.getImageUrl());
            }
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
            s3Service.deleteFile(star.getImageUrl());
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
        // 목록 조회에는 이미지 URL이 필요 없으므로 S3 변환 로직 제외
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

        // 2. D-Day 계산
        int dDay = (int) ChronoUnit.DAYS.between(star.getGalaxy().getStartDate(), star.getRecordDate()) + 1;
        
        // 3. 파일명을 URL로 변환
        String imageUrl = s3Service.getPresignedUrl(star.getImageUrl());
        
        // 4. DTO 생성 (Builder 사용)
        return StarResponseDto.StarDetail.builder()
                .starId(star.getId())
                .title(star.getTitle())
                .content(star.getContent())
                .recordDate(star.getRecordDate())
                .dDay(dDay)
                .imageUrl(imageUrl) // 변환된 URL 주입
                .cardColor(star.getCardColor())
                .emojis(star.getEmojis())
                .build();
    }
}