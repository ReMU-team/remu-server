package com.remu.domain.galaxy.controller;

import com.remu.domain.galaxy.dto.request.GalaxyReqDTO;
import com.remu.domain.galaxy.dto.response.GalaxyResDTO;
import com.remu.domain.galaxy.exception.code.GalaxySuccessCode;
import com.remu.domain.galaxy.service.GalaxyCommandService;
import com.remu.domain.user.entity.User;
import com.remu.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GalaxyController {
    private final GalaxyCommandService galaxyCommandService;

    // 1. 은하 생성
    // TODO 인증된 User 객체 넣기 @AuthenticationPrincipal
    @PostMapping("/galaxies")
    public ApiResponse<GalaxyResDTO.CreateDTO> createGalaxy(
            @RequestBody @Valid GalaxyReqDTO.CreateDTO request) {
        // 임시 유저
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .name("테스트유저")
                .build();
        return ApiResponse.onSuccess(GalaxySuccessCode.GALAXY_CREATED, galaxyCommandService.createGalaxy(request, mockUser));
    }

}
