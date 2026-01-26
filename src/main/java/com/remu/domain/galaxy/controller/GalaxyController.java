package com.remu.domain.galaxy.controller;

import com.remu.domain.galaxy.dto.request.GalaxyReqDTO;
import com.remu.domain.galaxy.dto.response.GalaxyResDTO;
import com.remu.domain.galaxy.exception.code.GalaxySuccessCode;
import com.remu.domain.galaxy.service.GalaxyCommandService;
import com.remu.domain.galaxy.service.GalaxyQueryService;
import com.remu.domain.user.entity.User;
import com.remu.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GalaxyController implements GalaxyControllerDocs {
    private final GalaxyCommandService galaxyCommandService;
    private final GalaxyQueryService galaxyQueryService;

    // 1. 은하 생성
    // TODO 인증된 User 객체 넣기 @AuthenticationPrincipal
    @PostMapping("/galaxies")
    public ApiResponse<GalaxyResDTO.CreateDTO> createGalaxy(
            @RequestBody @Valid GalaxyReqDTO.GalaxyCreateDTO request) {

        return ApiResponse.onSuccess(GalaxySuccessCode.GALAXY_CREATED, galaxyCommandService.createGalaxy(request, getMockUser()));
    }

    // 2. 은하 상세 조회
    @GetMapping("/galaxies/{galaxyId}")
    public ApiResponse<GalaxyResDTO.DetailDTO> getGalaxyDetail(
            @PathVariable Long galaxyId) {

        GalaxyResDTO.DetailDTO response = galaxyQueryService.getGalaxyDetail(galaxyId, getMockUser());
        return ApiResponse.onSuccess(GalaxySuccessCode.GALAXY_GET_SUCCESS, response);
    }

    // 3. 은하 전체 조회
    @GetMapping("/galaxies")
    public ApiResponse<GalaxyResDTO.SummaryListDTO> getGalaxyList(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size){


        // PageRequest를 통해 Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);

        return ApiResponse.onSuccess(GalaxySuccessCode.GALAXY_LIST_GET_SUCCESS,galaxyQueryService.getGalaxyList(getMockUser(), pageable));
    }

    // 4. 은하 삭제
    @DeleteMapping("/galaxies/{galaxyId}")
    public ApiResponse<Void> deleteGalaxy(@PathVariable Long galaxyId) {

        galaxyCommandService.deleteGalaxy(galaxyId, getMockUser());
        return ApiResponse.onSuccess(GalaxySuccessCode.GALAXY_DELETE_SUCCESS, null);
    }

    // 5. 은하 수정
    @PatchMapping("/galaxies/{galaxyId}")
    public ApiResponse<Void> updateGalaxy(
            @PathVariable Long galaxyId,
            @RequestBody GalaxyReqDTO.GalaxyUpdateDTO request
    ){
        galaxyCommandService.updateGalaxy(galaxyId, request, getMockUser());
        return ApiResponse.onSuccess(GalaxySuccessCode.GALAXY_UPDATE_SUCCESS, null);
    }


    /*
    TODO: delete
    Test User
     */
    private User getMockUser() {
        return User.builder()
                .id(1L)
                .email("test@test.com")
                .name("테스트유저")
                .build();
    }

}
