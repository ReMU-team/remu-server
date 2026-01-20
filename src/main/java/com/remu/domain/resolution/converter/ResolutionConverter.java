package com.remu.domain.resolution.converter;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.resolution.dto.ResolutionReqDTO;
import com.remu.domain.resolution.dto.ResolutionResDTO;
import com.remu.domain.resolution.entity.Resolution;
import org.springframework.data.domain.Page;

public class ResolutionConverter {

    /* ---------------------
     * [CREATE] 생성 관련 변환
     * --------------------- */

    // Entity -> DTO
    public static ResolutionResDTO.CreateDTO toCreateDTO(
            Resolution resolution
    ) {
        return ResolutionResDTO.CreateDTO.builder()
                .resolutionId(resolution.getId())
                .content(resolution.getContent())
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
     * [READ] 조회 관련 변환
     * --------------------- */

    public static ResolutionResDTO.ResolutionPreviewListDTO toResolutionPreviewListDTO(
            Page<Resolution> resolutions
    ) {
        return ResolutionResDTO.ResolutionPreviewListDTO.builder()
                .resolutionList(resolutions.stream()
                        .map(ResolutionConverter::toResolutionPreviewDTO)
                        .toList()
                )
                .listSize(resolutions.getSize())
                .totalPage(resolutions.getTotalPages())
                .totalElements(resolutions.getTotalElements())
                .isFirst(resolutions.isFirst())
                .isLast(resolutions.isLast())
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
