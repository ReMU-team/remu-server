package com.remu.domain.galaxy.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class GalaxyResDTO {
    // 생성
    public record CreateDTO (
            Long galaxyId,
            String name,
            LocalDate startDate,
            LocalDate arrivalDate,
            LocalDate endDate
    ){}

    // 상세 조회
    public record DetailDTO(
            Long galaxyId,
            String name,
            String emojiResourceName, // URL이 아닌 리소스 이름
            Long dDay,                // 계산된 D-Day
            LocalDate startDate,
            LocalDate arrivalDate,
            LocalDate endDate,
            String placeName
    ) {}

    // 목록 조회
    // 목록 내 개별 항목 정보
    public record SummaryDTO(
            Long galaxyId,
            String name,
            String emojiResourceName
    ) {}

    // 전체 목록 응답 (리스트를 감싸는 DTO)
    public record SummaryListDTO(
            Long totalCount,      // 사용자가 만든 은하 총 개수
            List<SummaryDTO> galaxies,
            Integer currentPage,
            Boolean hasNext // 다음 페이지 존재 여부

    ) {}


}
