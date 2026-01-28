package com.remu.domain.resolution.service;

import com.remu.domain.resolution.dto.ResolutionReqDTO;
import com.remu.domain.resolution.dto.ResolutionResDTO;

public interface ResolutionService {

    // === Command 로직 (상태 변경) ===
    ResolutionResDTO.ResolutionCreateDTO create(
            Long userId,
            Long galaxyId,
            ResolutionReqDTO.ResolutionCreateDTO dto
    );

    ResolutionResDTO.ResolutionBatchCreateDTO batchCreate(
            Long userId,
            Long galaxyId,
            ResolutionReqDTO.ResolutionBatchCreateDTO dto
    );

    ResolutionResDTO.ResolutionUpdateDTO update(
            Long userId,
            Long resolutionId,
            ResolutionReqDTO.ResolutionUpdateDTO dto
    );

    // 배치 다짐 수정
    ResolutionResDTO.ResolutionBatchCreateDTO batchUpdate(
            Long userId,
            Long galaxyId,
            ResolutionReqDTO.ResolutionBatchUpdateDTO dto
    );

    // === Query 로직 (조회) ===
    ResolutionResDTO.ResolutionPreviewListDTO findResolutions(
            Long userId, Long galaxyId
    );
}
