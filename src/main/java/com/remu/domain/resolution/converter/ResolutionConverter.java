package com.remu.domain.resolution.converter;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.resolution.dto.ResolutionReqDTO;
import com.remu.domain.resolution.dto.ResolutionResDTO;
import com.remu.domain.resolution.entity.Resolution;
import org.springframework.data.domain.Page;

import java.util.List;

public class ResolutionConverter {

    /* ---------------------
     * [CREATE] 단일 생성 관련 변환
     * --------------------- */

    // Entity -> DTO
    public static ResolutionResDTO.CreateDTO toCreateDTO(
            Resolution resolution
    ) {
        return ResolutionResDTO.CreateDTO.builder()
                .resolutionId(resolution.getId())
                .content(resolution.getContent())
                .emojiId(resolution.getGalaxy().getResolutionEmojiId())
                .createdAt(resolution.getCreatedAt())
                .build();
    }

    // DTO -> Entity
    public static Resolution toResolution(
            ResolutionReqDTO.CreateDTO dto, Galaxy galaxy
    ) {
        return Resolution.builder()
                .galaxy(galaxy)
                .content(dto.content())
                .build();
    }

    /* ---------------------
     * [CREATE] 배치 생성 관련 변환
     * --------------------- */

    // DTO -> Entity
    public static Resolution toResolutionFromBatch(String content, Galaxy galaxy) {
        return Resolution.builder()
                .galaxy(galaxy)
                .content(content)
                .build();
    }

    // Entity -> DTO
    public static ResolutionResDTO.BatchCreateDTO toBatchCreateDTO(
            Galaxy galaxy,
            List<Resolution> resolutions
    ) {
        List<ResolutionResDTO.SingleResolutionDTO> list = resolutions.stream()
                .map(res -> ResolutionResDTO.SingleResolutionDTO.builder()
                        .resolutionId(res.getId())
                        .content(res.getContent())
                        .createdAt(res.getCreatedAt())
                        .build())
                .toList();

        return ResolutionResDTO.BatchCreateDTO.builder()
                .emojiId(galaxy.getResolutionEmojiId())
                .illustId(galaxy.getResolutionIllustId())
                .resolutions(list)
                .build();
    }

    /* ---------------------
     * [READ] 조회 관련 변환
     * --------------------- */

    public static ResolutionResDTO.ResolutionPreviewListDTO toResolutionPreviewListDTO(
            List<Resolution> resolutions, String emojiId, String illustId
    ) {
        return ResolutionResDTO.ResolutionPreviewListDTO.builder()
                .emojiId(emojiId)
                .illustId(illustId)
                .resolutionList(resolutions.stream()
                        .map(ResolutionConverter::toResolutionPreviewDTO)
                        .toList()
                )
                .listSize(resolutions.size())
                .build();
    }

    public static ResolutionResDTO.ResolutionPreviewDTO toResolutionPreviewDTO(
            Resolution resolution
    ) {
        return ResolutionResDTO.ResolutionPreviewDTO.builder()
                .resolutionId(resolution.getId())
                .content(resolution.getContent())
                .createdAt(resolution.getCreatedAt())
                .build();
    }

    /* ---------------------
     * [UPDATE] 수정 관련 변환
     * --------------------- */

    public static ResolutionResDTO.UpdateDTO toUpdateDTO(Resolution resolution) {
        return ResolutionResDTO.UpdateDTO.builder()
                .resolutionId(resolution.getId())
                .content(resolution.getContent())
                .updatedAt(resolution.getUpdatedAt())
                .build();
    }
}
