package com.remu.domain.galaxy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class GalaxyReqDTO {
    // 은하 생성 요청 DTO
    public record GalaxyCreateDTO(
            @NotBlank(message = "은하 이름은 필수입니다.")
            String name,

            @NotNull(message = "시작 날짜는 필수입니다.")
            LocalDate startDate,

            @NotNull(message = "종료 날짜는 필수입니다.")
            LocalDate endDate,

            @NotNull(message = "대표 이모지는 필수입니다.")
            String emojiResourceName,

            @NotBlank(message = "장소 ID는 필수입니다.")
            String googlePlaceId,

            // 사용자가 선택한 장소 이름
            String placeName

    ){}

    // 은하 정보 수정 요청
    public record GalaxyUpdateDTO(
            String name,
            String emojiResourceName,
            LocalDate startDate,
            LocalDate endDate,
            String googlePlaceId,
            String placeName
    ){}
}
