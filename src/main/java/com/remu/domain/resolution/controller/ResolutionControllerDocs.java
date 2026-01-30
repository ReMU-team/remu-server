package com.remu.domain.resolution.controller;

import com.remu.domain.resolution.dto.ResolutionReqDTO;
import com.remu.domain.resolution.dto.ResolutionResDTO;
import com.remu.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

public interface ResolutionControllerDocs {

    // 다짐 배치 생성
    @Operation(
            summary = "다짐 배치 생성 API by 매튜/진현준",
            description = "다짐 배치 생성 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<ResolutionResDTO.ResolutionBatchCreateDTO> createResolutionBatch(
            @RequestParam Long userId,
            @PathVariable Long galaxyId,
            @Valid @RequestBody ResolutionReqDTO.ResolutionBatchCreateDTO dto
    );

    // 다짐 목록 조회
    @Operation(
            summary = "다짐 목록들을 조회하는 API by 매튜/진현준",
            description = "다짐을 조회하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<ResolutionResDTO.ResolutionPreviewListDTO> getResolutions(
            @RequestParam Long userId,
            @PathVariable Long galaxyId
    );

    // 다짐 배치 수정
    @Operation(
            summary = "다짐 목록들을 배치 처리해 수정하는 API by 매튜/진현준",
            description = "다짐들을 배치 처리해 수정하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<ResolutionResDTO.ResolutionBatchCreateDTO> updateResolutionBatch(
            @RequestParam Long userId,
            @PathVariable Long galaxyId,
            @Valid @RequestBody ResolutionReqDTO.ResolutionBatchUpdateDTO dto
    );
}
