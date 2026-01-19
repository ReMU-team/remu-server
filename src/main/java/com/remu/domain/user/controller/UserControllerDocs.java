package com.remu.domain.user.controller;

import com.remu.domain.user.dto.req.UserReqDTO;
import com.remu.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserControllerDocs {

    @Operation(
            summary = "프로필 생성 & 프로필 설정 API",
            description = "프로필 사진, 이름, 한 줄 소개를 만들거나 변경합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")

    })
    ApiResponse<Void> updateProfile(Long userId, @RequestBody @Valid UserReqDTO.ProfileDTO dto);
}
