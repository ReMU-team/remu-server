package com.remu.domain.galaxy.controller;

import com.remu.domain.galaxy.dto.request.GalaxyReqDTO;
import com.remu.domain.galaxy.dto.response.GalaxyResDTO;
import com.remu.domain.galaxy.exception.code.GalaxySuccessCode;
import com.remu.domain.galaxy.service.GalaxyCommandService;
import com.remu.domain.galaxy.service.GalaxyQueryService;
import com.remu.global.apiPayload.ApiResponse;
import com.remu.global.config.sercurity.oauth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/galaxies")
public class GalaxyController implements GalaxyControllerDocs {
    private final GalaxyCommandService galaxyCommandService;
    private final GalaxyQueryService galaxyQueryService;

    // 1. 은하 생성
    @PostMapping
    public ApiResponse<GalaxyResDTO.GalaxyCreateDTO> createGalaxy(
            @RequestBody @Valid GalaxyReqDTO.GalaxyCreateDTO request, @AuthenticationPrincipal(expression = "id") Long userId) {

        return ApiResponse.onSuccess(GalaxySuccessCode.GALAXY_CREATED, galaxyCommandService.createGalaxy(request, userId));
    }

    // 2. 은하 상세 조회
    @GetMapping("/{galaxyId}")
    public ApiResponse<GalaxyResDTO.GalaxyDetailDTO> getGalaxyDetail(
            @PathVariable Long galaxyId, @AuthenticationPrincipal(expression = "id") Long userId) {

        GalaxyResDTO.GalaxyDetailDTO response = galaxyQueryService.getGalaxyDetail(galaxyId, userId);
        return ApiResponse.onSuccess(GalaxySuccessCode.GALAXY_GET_SUCCESS, response);
    }

    // 3. 은하 전체 조회
    @GetMapping
    public ApiResponse<GalaxyResDTO.SummaryListDTO> getGalaxyList(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @AuthenticationPrincipal(expression = "id") Long userId){


        // PageRequest를 통해 Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);

        return ApiResponse.onSuccess(GalaxySuccessCode.GALAXY_LIST_GET_SUCCESS,galaxyQueryService.getGalaxyList(userId, pageable));
    }

    // 4. 은하 삭제
    @DeleteMapping("/{galaxyId}")
    public ApiResponse<Void> deleteGalaxy(@PathVariable Long galaxyId, @AuthenticationPrincipal(expression = "id") Long userId) {

        galaxyCommandService.deleteGalaxy(galaxyId, userId);
        return ApiResponse.onSuccess(GalaxySuccessCode.GALAXY_DELETE_SUCCESS, null);
    }

    // 5. 은하 수정
    @PatchMapping("/{galaxyId}")
    public ApiResponse<Void> updateGalaxy(
            @PathVariable Long galaxyId,
            @RequestBody GalaxyReqDTO.GalaxyUpdateDTO request, @AuthenticationPrincipal(expression = "id") Long userId
    ){
        galaxyCommandService.updateGalaxy(galaxyId, request, userId);
        return ApiResponse.onSuccess(GalaxySuccessCode.GALAXY_UPDATE_SUCCESS, null);
    }


}
