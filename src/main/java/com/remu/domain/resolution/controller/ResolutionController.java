package com.remu.domain.resolution.controller;

import com.remu.domain.resolution.dto.ResolutionReqDTO;
import com.remu.domain.resolution.dto.ResolutionResDTO;
import com.remu.domain.resolution.exception.code.ResolutionSuccessCode;
import com.remu.domain.resolution.service.ResolutionService;
import com.remu.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/galaxies/{galaxyId}/resolutions")
public class ResolutionController implements ResolutionControllerDocs{

    private final ResolutionService resolutionService;

    // 다짐 배치 생성
    @PostMapping
    @Override
    public ApiResponse<ResolutionResDTO.ResolutionBatchCreateDTO> createResolutionBatch(
            @RequestParam Long userId,
            @PathVariable Long galaxyId,
            @Valid @RequestBody ResolutionReqDTO.ResolutionBatchCreateDTO dto
    ) {
        ResolutionResDTO.ResolutionBatchCreateDTO result = resolutionService.batchCreate(userId, galaxyId, dto);
        return ApiResponse.onSuccess(ResolutionSuccessCode.CREATE, result);
    }

    // 다짐 목록 조회
    @GetMapping
    @Override
    public ApiResponse<ResolutionResDTO.ResolutionPreviewListDTO> getResolutions(
            @RequestParam Long userId,
            @PathVariable Long galaxyId
    ) {
        ResolutionResDTO.ResolutionPreviewListDTO result = resolutionService.findResolutions(userId, galaxyId);
        return ApiResponse.onSuccess(ResolutionSuccessCode.FOUND, result);
    }

    // 다짐 배치 수정
    @PatchMapping
    @Override
    public ApiResponse<ResolutionResDTO.ResolutionBatchCreateDTO> updateResolutionBatch(
            @RequestParam Long userId,
            @PathVariable Long galaxyId,
            @Valid @RequestBody ResolutionReqDTO.ResolutionBatchUpdateDTO dto
    ) {
        ResolutionResDTO.ResolutionBatchCreateDTO result = resolutionService.batchUpdate(userId, galaxyId, dto);
        return ApiResponse.onSuccess(ResolutionSuccessCode.UPDATE, result);
    }
}
