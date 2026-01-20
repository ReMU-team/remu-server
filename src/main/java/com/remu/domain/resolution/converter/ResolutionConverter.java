package com.remu.domain.resolution.converter;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.resolution.dto.ResolutionReqDTO;
import com.remu.domain.resolution.dto.ResolutionResDTO;
import com.remu.domain.resolution.entity.Resolution;

public class ResolutionConverter {

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
}
