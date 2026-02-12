package com.remu.domain.star.controller;

import com.remu.domain.star.dto.request.StarCreateRequest;
import com.remu.domain.star.dto.request.StarUpdateRequest;
import com.remu.domain.star.dto.response.StarResponseDto;
import com.remu.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StartControllerDocs {
    // 별 생성
    @Operation(
            summary = "별 생성 API by 웅표빠잉/이웅재",
            description = "별 생성 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<Long> createStar(
            @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @Valid @RequestPart("request") StarCreateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    );

    // 별 수정
    @Operation(
            summary = "별 수정 API by 웅표빠잉/이웅재",
            description = "별 수정 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<Long> updateStar(
            @PathVariable("starId") Long starId,
            @Valid @RequestPart("request") StarUpdateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    );

    // 별 삭제
    @Operation(
            summary = "별 삭제 API by 웅표빠잉/이웅재",
            description = "별 삭제 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    @DeleteMapping("/stars/{starId}")
    ApiResponse<String> deleteStar(@PathVariable("starId") Long starId);

    // 은하별 별 목록 조회
    @Operation(
            summary = "은하별 별 목록 조회 API by 웅표빠잉/이웅재",
            description = "은하별 별 목록 조회 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<List<StarResponseDto.StarPreview>> getStarsByGalaxyId(@PathVariable("galaxyId") Long galaxyId);

    // 별 상세 조회
    @Operation(
            summary = "별 상세 조회 API by 웅표빠잉/이웅재",
            description = "별 상세 조회 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<StarResponseDto.StarDetail> getStarDetail(@PathVariable("starId") Long starId);
}
