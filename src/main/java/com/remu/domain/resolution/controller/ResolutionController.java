package com.remu.domain.resolution.controller;

import com.remu.domain.resolution.dto.ResolutionReqDTO;
import com.remu.domain.resolution.dto.ResolutionResDTO;
import com.remu.domain.resolution.exception.code.ResolutionSuccessCode;
import com.remu.domain.resolution.service.ResolutionService;
import com.remu.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ResolutionController implements ResolutionControllerDocs{

    private final ResolutionService resolutionService;

    @Override
    @PostMapping("/create-resolution")
    public ApiResponse<ResolutionResDTO.CreateDTO> create (
            @Valid @RequestBody ResolutionReqDTO.CreateDTO dto
    ) {
        ResolutionResDTO.CreateDTO result = resolutionService.create(dto);
        return ApiResponse.onSuccess(ResolutionSuccessCode.CREATE, result);
    }
}
