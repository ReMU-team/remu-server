package com.remu.domain.resolution.converter;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.resolution.dto.ResolutionReqDTO;
import com.remu.domain.resolution.dto.ResolutionResDTO;
import com.remu.domain.resolution.entity.Resolution;

import java.util.List;

public class ResolutionConverter {

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
    public static ResolutionResDTO.ResolutionBatchCreateDTO toBatchCreateDTO(
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

        return ResolutionResDTO.ResolutionBatchCreateDTO.builder()
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
}
