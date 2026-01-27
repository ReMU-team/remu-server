package com.remu.domain.star.converter;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.star.dto.request.StarCreateRequest;
import com.remu.domain.star.dto.response.StarResponseDto;
import com.remu.domain.star.entity.Star;

public class StarConverter {

    // Request -> Entity (별 생성)
    public static Star toStar(StarCreateRequest request, Galaxy galaxy, String imageUrl) {
        return Star.builder()
                .galaxy(galaxy)
                .title(request.getTitle())
                .content(request.getContent())
                .recordDate(request.getRecordDate())
                .cardColor(request.getCardColor())
                .imageUrl(imageUrl)
                .emojis(request.getEmojis())
                .build();
    }

    // Entity -> Preview DTO (목록 조회)
    public static StarResponseDto.StarPreview toStarPreview(Star star, Integer dDay) {
        return StarResponseDto.StarPreview.builder()
                .starId(star.getId())
                .title(star.getTitle())
                .recordDate(star.getRecordDate())
                .dDay(dDay)
                .cardColor(star.getCardColor())
                .build();
    }

    // Entity -> Detail DTO (상세 조회)
    public static StarResponseDto.StarDetail toStarDetail(Star star, Integer dDay) {
        return StarResponseDto.StarDetail.builder()
                .starId(star.getId())
                .title(star.getTitle())
                .content(star.getContent())
                .recordDate(star.getRecordDate())
                .dDay(dDay)
                .imageUrl(star.getImageUrl())
                .cardColor(star.getCardColor())
                .emojis(star.getEmojis())
                .build();
    }
}