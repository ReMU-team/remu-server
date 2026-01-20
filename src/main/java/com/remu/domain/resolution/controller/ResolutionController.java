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
    @PostMapping("/create-resolution")
    public ApiResponse<ResolutionResDTO.CreateDTO> create (
            @Valid @RequestBody ResolutionReqDTO.CreateDTO dto
    ) {
        ResolutionResDTO.CreateDTO result = resolutionService.create(dto);
        return ApiResponse.onSuccess(ResolutionSuccessCode.CREATE, result);
    }

    // 다짐 목록 조회
    @GetMapping("/galaxies/{galaxyId}/resolutions")
    @Override
    public ApiResponse<ResolutionResDTO.ResolutionPreviewListDTO> getResolutions(
            @RequestParam Long userId,
            @PathVariable Long galaxyId,
            @RequestParam Integer page
    ) {
        ResolutionResDTO.ResolutionPreviewListDTO result = resolutionService.findResolutions(userId, galaxyId, page);
        return ApiResponse.onSuccess(ResolutionSuccessCode.FOUND, result);
    }
}
