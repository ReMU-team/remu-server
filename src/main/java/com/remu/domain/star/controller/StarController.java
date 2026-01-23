package com.remu.domain.star.controller;

import com.remu.domain.star.dto.request.StarCreateRequest;
import com.remu.domain.star.dto.response.StarResponseDto;
import com.remu.domain.star.service.StarService;
import com.remu.global.apiPayload.ApiResponse;
import com.remu.global.apiPayload.code.GeneralSuccessCode;
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
            @Valid @RequestPart("request") StarCreateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        request.setImage(image);
        Long starId = starService.createStar(request);
        return ApiResponse.onSuccess(GeneralSuccessCode._CREATED, starId);
    }

    // 은하별 별 목록 조회
    @GetMapping("/galaxies/{galaxyId}/stars")
    public ApiResponse<List<StarResponseDto.StarPreview>> getStarsByGalaxyId(@PathVariable("galaxyId") Long galaxyId) {
        List<StarResponseDto.StarPreview> stars = starService.getStarsByGalaxyId(galaxyId);
        return ApiResponse.onSuccess(GeneralSuccessCode._OK, stars);
    }

    // 별 상세 조회
    @GetMapping("/stars/{starId}")
    public ApiResponse<StarResponseDto.StarDetail> getStarDetail(@PathVariable("starId") Long starId) {
        StarResponseDto.StarDetail starDetail = starService.getStarDetail(starId);
        return ApiResponse.onSuccess(GeneralSuccessCode._OK, starDetail);
    }
}