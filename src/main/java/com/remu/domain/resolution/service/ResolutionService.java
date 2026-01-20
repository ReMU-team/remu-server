package com.remu.domain.resolution.service;

import com.remu.domain.resolution.dto.ResolutionReqDTO;
import com.remu.domain.resolution.dto.ResolutionResDTO;

public interface ResolutionService {

    // === Command 로직 (상태 변경) ===
    ResolutionResDTO.CreateDTO create(
            ResolutionReqDTO.CreateDTO dto
    );

    // === Query 로직 (조회) ===
    ResolutionResDTO.ResolutionPreviewListDTO findResolutions(
            Long userId, Long galaxyId, Integer page
    );
}
