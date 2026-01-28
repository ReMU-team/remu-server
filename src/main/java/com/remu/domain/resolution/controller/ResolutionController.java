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
public class ResolutionController implements ResolutionControllerDocs{

    private final ResolutionService resolutionService;

    // 다짐 생성
    @Override
    @PostMapping("/galaxies/{galaxyId}/resolutions")
    public ApiResponse<ResolutionResDTO.CreateDTO> create (
            @RequestParam Long userId,
            @PathVariable Long galaxyId,
            @Valid @RequestBody ResolutionReqDTO.CreateDTO dto
    ) {
        ResolutionResDTO.CreateDTO result = resolutionService.create(userId, galaxyId, dto);
        return ApiResponse.onSuccess(ResolutionSuccessCode.CREATE, result);
    }

    // 다짐 배치 생성
    @Override
    @PostMapping("/galaxies/{galaxyId}/resolutions/batch")
    public ApiResponse<ResolutionResDTO.BatchCreateDTO> createBatch(
            @RequestParam Long userId,
            @PathVariable Long galaxyId,
            @Valid @RequestBody ResolutionReqDTO.BatchCreateDTO dto
    ) {
        ResolutionResDTO.BatchCreateDTO result = resolutionService.batchCreate(userId, galaxyId, dto);
        return ApiResponse.onSuccess(ResolutionSuccessCode.CREATE, result);
    }

    // 다짐 목록 조회
    @Override
    @GetMapping("/galaxies/{galaxyId}/resolutions")
    public ApiResponse<ResolutionResDTO.ResolutionPreviewListDTO> getResolutions(
            @RequestParam Long userId,
            @PathVariable Long galaxyId
    ) {
        ResolutionResDTO.ResolutionPreviewListDTO result = resolutionService.findResolutions(userId, galaxyId);
        return ApiResponse.onSuccess(ResolutionSuccessCode.FOUND, result);
    }

    // 다짐 수정
    @PatchMapping("/resolutions/{resolutionId}")
    @Override
    public ApiResponse<ResolutionResDTO.UpdateDTO> updateResolution(
            @RequestParam Long userId,
            @PathVariable Long resolutionId,
            @RequestBody ResolutionReqDTO.UpdateDTO dto
    ) {
        ResolutionResDTO.UpdateDTO result = resolutionService.update(userId, resolutionId, dto);
        return ApiResponse.onSuccess(ResolutionSuccessCode.UPDATE, result);
    }
}
