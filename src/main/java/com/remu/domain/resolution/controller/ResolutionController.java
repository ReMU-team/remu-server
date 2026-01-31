package com.remu.domain.resolution.controller;

import com.remu.domain.resolution.dto.ResolutionReqDTO;
import com.remu.domain.resolution.dto.ResolutionResDTO;
import com.remu.domain.resolution.exception.code.ResolutionSuccessCode;
import com.remu.domain.resolution.service.ResolutionService;
import com.remu.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ResolutionController implements ResolutionControllerDocs{

    private final ResolutionService resolutionService;

    // 다짐 생성
    @Override
    @PostMapping("/galaxies/{galaxyId}/resolutions")
    public ApiResponse<ResolutionResDTO.ResolutionCreateDTO> createResolution (
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable Long galaxyId,
            @Valid @RequestBody ResolutionReqDTO.ResolutionCreateDTO dto
    ) {
        ResolutionResDTO.ResolutionCreateDTO result = resolutionService.create(userId, galaxyId, dto);
        return ApiResponse.onSuccess(ResolutionSuccessCode.CREATE, result);
    }

    // 다짐 배치 생성
    @Override
    @PostMapping("/galaxies/{galaxyId}/resolutions/batch")
    public ApiResponse<ResolutionResDTO.ResolutionBatchCreateDTO> createResolutionBatch(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable Long galaxyId,
            @Valid @RequestBody ResolutionReqDTO.ResolutionBatchCreateDTO dto
    ) {
        ResolutionResDTO.ResolutionBatchCreateDTO result = resolutionService.batchCreate(userId, galaxyId, dto);
        return ApiResponse.onSuccess(ResolutionSuccessCode.CREATE, result);
    }

    // 다짐 목록 조회
    @Override
    @GetMapping("/galaxies/{galaxyId}/resolutions")
    public ApiResponse<ResolutionResDTO.ResolutionPreviewListDTO> getResolutions(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable Long galaxyId
    ) {
        ResolutionResDTO.ResolutionPreviewListDTO result = resolutionService.findResolutions(userId, galaxyId);
        return ApiResponse.onSuccess(ResolutionSuccessCode.FOUND, result);
    }

    // 다짐 수정
    @PatchMapping("/resolutions/{resolutionId}")
    @Override
    public ApiResponse<ResolutionResDTO.ResolutionUpdateDTO> updateResolution(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable Long resolutionId,
            @RequestBody ResolutionReqDTO.ResolutionUpdateDTO dto
    ) {
        ResolutionResDTO.ResolutionUpdateDTO result = resolutionService.update(userId, resolutionId, dto);
        return ApiResponse.onSuccess(ResolutionSuccessCode.UPDATE, result);
    }

    // 다짐 배치 수정
    @PatchMapping("/galaxies/{galaxyId}/resolutions/batch")
    @Override
    public ApiResponse<ResolutionResDTO.ResolutionBatchCreateDTO> updateResolutionBatch(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable Long galaxyId,
            @Valid @RequestBody ResolutionReqDTO.ResolutionBatchUpdateDTO dto
    ) {
        ResolutionResDTO.ResolutionBatchCreateDTO result = resolutionService.batchUpdate(userId, galaxyId, dto);
        return ApiResponse.onSuccess(ResolutionSuccessCode.UPDATE, result);
    }
}
