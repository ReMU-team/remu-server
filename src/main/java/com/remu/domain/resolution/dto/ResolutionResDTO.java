package com.remu.domain.resolution.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

// Res: 유저의 요청에 대한 시스템 응답

public class ResolutionResDTO {

    // 다짐 생성 요청에 대한 응답 결과
    @Builder
    public record ResolutionCreateDTO(
            Long resolutionId,
            String content,
            String emojiId,
            LocalDateTime createdAt
    ) {}

    // 다짐 생성 요청 배치 처리
    @Builder
    public record ResolutionBatchCreateDTO(
            String emojiId,
            String illustId,
            List<SingleResolutionDTO> resolutions
    ) {}

    @Builder
    public record SingleResolutionDTO(
            Long resolutionId,
            String content,
            LocalDateTime createdAt
    ) {}


    // 다짐 조회 DTO
    @Builder
    public record ResolutionPreviewListDTO(
        String emojiId,
        String illustId,
        List<ResolutionPreviewDTO> resolutionList,
        Integer listSize
    ) {}

    @Builder
    public record ResolutionPreviewDTO(
            Long resolutionId,
            String content,
            LocalDateTime createdAt
    ) {}

    // 다짐 수정 DTO
    @Builder
    public record ResolutionUpdateDTO(
            Long resolutionId,
            String content,
            LocalDateTime updatedAt
    ) {}
}
