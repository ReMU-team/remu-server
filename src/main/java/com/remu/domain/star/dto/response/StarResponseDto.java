package com.remu.domain.star.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class StarResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StarDetail {
        private Long starId;
        private String title;
        private String content;
        private LocalDate recordDate;
        private Integer dDay; // 여행 시작일로부터 며칠째인지 (Day N)
        private String imageUrl;
        private String cardColor; // 카드 색상 (String)
        private List<String> emojis; // 이모지 이름 리스트 (String)
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StarPreview {
        private Long starId;
        private String title;
        private LocalDate recordDate;
        private Integer dDay; // 여행 시작일로부터 며칠째인지 (Day N)
        private String cardColor; // 카드 색상 (String)
    }
}