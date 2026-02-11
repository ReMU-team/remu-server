package com.remu.domain.star.controller;

import com.remu.domain.star.dto.request.StarCreateRequest;
import com.remu.domain.star.dto.request.StarUpdateRequest;
import com.remu.domain.star.dto.response.StarResponseDto;
import com.remu.domain.star.exception.code.StarSuccessCode;
import com.remu.domain.star.service.StarService;
import com.remu.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StarController {

    private final StarService starService;

    // 별 생성
    @PostMapping(value = "/stars", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Long> createStar(
            @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @Valid @RequestPart("request") StarCreateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        request.setImage(image);
        Long starId = starService.createStar(request);
        return ApiResponse.onSuccess(StarSuccessCode.STAR_CREATED, starId);
    }

    // 별 수정
    @PatchMapping(value = "/stars/{starId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Long> updateStar(
            @PathVariable("starId") Long starId,
            @Valid @RequestPart("request") StarUpdateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        request.setImage(image);
        Long updatedStarId = starService.updateStar(starId, request);
        return ApiResponse.onSuccess(StarSuccessCode.STAR_UPDATED, updatedStarId);
    }

    // 별 삭제
    @DeleteMapping("/stars/{starId}")
    public ApiResponse<String> deleteStar(@PathVariable("starId") Long starId) {
        starService.deleteStar(starId);
        return ApiResponse.onSuccess(StarSuccessCode.STAR_DELETED, "삭제되었습니다.");
    }

    // 은하별 별 목록 조회
    @GetMapping("/galaxies/{galaxyId}/stars")
    public ApiResponse<List<StarResponseDto.StarPreview>> getStarsByGalaxyId(@PathVariable("galaxyId") Long galaxyId) {
        List<StarResponseDto.StarPreview> stars = starService.getStarsByGalaxyId(galaxyId);
        return ApiResponse.onSuccess(StarSuccessCode.STAR_LIST_SEARCH, stars);
    }

    // 별 상세 조회
    @GetMapping("/stars/{starId}")
    public ApiResponse<StarResponseDto.StarDetail> getStarDetail(@PathVariable("starId") Long starId) {
        StarResponseDto.StarDetail starDetail = starService.getStarDetail(starId);
        return ApiResponse.onSuccess(StarSuccessCode.STAR_DETAIL_SEARCH, starDetail);
    }
}