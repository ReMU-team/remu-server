package com.remu.domain.resolution.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

// Res: 유저의 요청에 대한 시스템 응답

public class ResolutionResDTO {

    // 다짐 생성 요청에 대한 응답 결과
    @Builder
    public record CreateDTO(
            Long resolutionId,
            String content,
            LocalDateTime createdAt
    ) {}

    @Builder
    public record ResolutionPreviewListDTO(
        List<ResolutionPreviewDTO> resolutionList,
        Integer listSize
    ) {}

    @Builder
    public record ResolutionPreviewDTO(
            Long resolutionId,
            String content,
            LocalDateTime createdAt
    ) {}
}
