package com.remu.domain.resolution.controller;

import com.remu.domain.resolution.dto.ResolutionReqDTO;
import com.remu.domain.resolution.dto.ResolutionResDTO;
import com.remu.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

public interface ResolutionControllerDocs {

    @Operation(
            summary = "다짐을 생성하는 API by 매튜/진현준",
            description = "다짐을 생성하는 API입니다. 생성 과정에 userId, galaxyId를 통해 유효한 사용자인지 검증합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<ResolutionResDTO.CreateDTO> createResolution(
            @RequestParam Long userId,
            @PathVariable Long galaxyId,
            ResolutionReqDTO.CreateDTO dto
    );

    // 다짐 배치 생성
    @Operation(
            summary = "다짐 배치 생성 API by 매튜/진현준",
            description = "다짐 배치 생성 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<ResolutionResDTO.BatchCreateDTO> createResolutionBatch(
            @RequestParam Long userId,
            @PathVariable Long galaxyId,
            @Valid @RequestBody ResolutionReqDTO.BatchCreateDTO dto
    );

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


    // 다짐 수정

    @Operation(
            summary = "다짐 목록을 수정하는 API by 매튜/진현준",
            description = "다짐을 수정하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<ResolutionResDTO.UpdateDTO> updateResolution(
            @RequestParam Long userId,
            @PathVariable Long resolutionId,
            @RequestBody ResolutionReqDTO.UpdateDTO dto
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
    @PatchMapping("/galaxies/{galaxyId}/resolutions/batch")
    ApiResponse<ResolutionResDTO.BatchCreateDTO> updateResolutionBatch(
            @RequestParam Long userId,
            @PathVariable Long galaxyId,
            @Valid @RequestBody ResolutionReqDTO.BatchUpdateDTO dto
    );
}
