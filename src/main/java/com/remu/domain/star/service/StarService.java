package com.remu.domain.star.service;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.galaxy.repository.GalaxyRepository;
import com.remu.domain.star.dto.request.StarCreateRequest;
import com.remu.domain.star.dto.response.StarResponseDto;
import com.remu.domain.star.entity.Star;
import com.remu.domain.star.repository.StarRepository;
import jakarta.persistence.EntityNotFoundException;
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
                .orElseThrow(() -> new EntityNotFoundException("은하를 찾을 수 없습니다."));

        // 2. 이미지 업로드
        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            // imageUrl = s3Service.upload(request.getImage());
            imageUrl = "https://dummy-s3-url.com/image.jpg"; // 임시
        }

        // 3. Star 엔티티 생성 및 저장
        Star star = Star.builder()
                .galaxy(galaxy)
                .title(request.getTitle())
                .content(request.getContent())
                .recordDate(request.getRecordDate())
                .cardColor(request.getCardColor()) // String 그대로 저장
                .imageUrl(imageUrl)
                .emojis(request.getEmojis()) // List<String> 그대로 저장
                .build();
        
        starRepository.save(star);

        return star.getId();
    }

    // 은하별 별 목록 조회
    public List<StarResponseDto.StarPreview> getStarsByGalaxyId(Long galaxyId) {
        // 1. 은하 존재 여부 확인
        Galaxy galaxy = galaxyRepository.findById(galaxyId)
                .orElseThrow(() -> new EntityNotFoundException("은하를 찾을 수 없습니다."));

        // 2. 별 목록 조회
        List<Star> stars = starRepository.findAllByGalaxyId(galaxyId);

        // 3. DTO 변환
        return stars.stream()
                .map(star -> StarResponseDto.StarPreview.builder()
                        .starId(star.getId())
                        .title(star.getTitle())
                        .recordDate(star.getRecordDate())
                        .dDay((int) ChronoUnit.DAYS.between(galaxy.getStartDate(), star.getRecordDate()) + 1)
                        .cardColor(star.getCardColor())
                        .build())
                .collect(Collectors.toList());
    }

    // 별 상세 조회
    public StarResponseDto.StarDetail getStarDetail(Long starId) {
        // 1. 별 조회 (Fetch Join으로 Galaxy 함께 조회)
        Star star = starRepository.findByIdFetchJoin(starId)
                .orElseThrow(() -> new EntityNotFoundException("별을 찾을 수 없습니다."));

        // 2. DTO 변환
        return StarResponseDto.StarDetail.builder()
                .starId(star.getId())
                .title(star.getTitle())
                .content(star.getContent())
                .recordDate(star.getRecordDate())
                .dDay((int) ChronoUnit.DAYS.between(star.getGalaxy().getStartDate(), star.getRecordDate()) + 1)
                .imageUrl(star.getImageUrl())
                .cardColor(star.getCardColor())
                .emojis(star.getEmojis()) // List<String> 그대로 반환
                .build();
    }
}