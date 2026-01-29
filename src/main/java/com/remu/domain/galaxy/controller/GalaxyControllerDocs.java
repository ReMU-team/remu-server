package com.remu.domain.galaxy.controller;

import com.remu.domain.galaxy.dto.request.GalaxyReqDTO;
import com.remu.domain.galaxy.dto.response.GalaxyResDTO;
import com.remu.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface GalaxyControllerDocs {
    // 1. 은하 생성
    @Operation(
            summary = "은하 생성하는 API by 요시/김희서"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<GalaxyResDTO.GalaxyCreateDTO> createGalaxy(@RequestBody @Valid GalaxyReqDTO.GalaxyCreateDTO request);

    // 2. 은하 상세 조회
    @Operation(
            summary = "은하 상세 조회하는 API by 요시/김희서",
            description = "선택된 은하id 기반으로 상세 정보를 불러옵니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<GalaxyResDTO.GalaxyDetailDTO> getGalaxyDetail(@PathVariable Long galaxyId);

    // 3. 은하 전체 조회
    @Operation(
            summary = "사용자기반 은하 목록 조회하는 API by 요시/김희서",
            description = "사용자 기반으로 전체 목록 및 총 개수를 불러옵니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<GalaxyResDTO.SummaryListDTO> getGalaxyList(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size);


    // 4. 은하 삭제
    @Operation(
            summary = "은하 삭제하는 API by 요시/김희서",
            description = "은하를 영구 삭제합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<Void> deleteGalaxy(@PathVariable Long galaxyId);

    // 5. 은하 수정
    @Operation(
            summary = "은하 수정하는 API by 요시/김희서"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<Void> updateGalaxy(
            @PathVariable Long galaxyId,
            @RequestBody GalaxyReqDTO.GalaxyUpdateDTO request);

}
