package com.remu.domain.user.controller;

import com.remu.domain.user.dto.res.UserResDTO;
import com.remu.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface UserControllerDocs {

    @Operation(
            summary = "프로필 생성 & 프로필 수정 API",
            description = "프로필 사진, 이름, 한 줄 소개를 생성하거나 변경합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")

    })
    ApiResponse<Void> updateProfile(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestPart("name") String name,
            @RequestPart(value = "introduction", required = false) String introduction,
            @RequestPart(value = "image", required = false) MultipartFile image
    );

    @Operation(
            summary = "닉네임 사용 가능 여부 검증 API",
            description = "길이가 2자 ~ 15자 사이이고 중복된 닉네임이 아닌지 확인합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")

    })
    UserResDTO.NameCheckDTO checkName(
            @RequestParam(required = false) String name,
            @AuthenticationPrincipal(expression = "id") Long userId
    );

    @Operation(
            summary = "프로필 조회 API",
            description = "프로필 사진, 이름, 한 줄 소개를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")

    })
    ApiResponse<UserResDTO.ProfileDTO> getProfile(
            @AuthenticationPrincipal(expression = "id") Long userId
    );

    @Operation(
            summary = "회원 탈퇴 API",
            description = "회원 탈퇴를 진행합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")

    })
    ApiResponse<Void> deleteAccount(
            @AuthenticationPrincipal(expression = "id") Long userId
    );

    @Operation(
            summary = "알림 설정 변경 API",
            description = "알림 수신 여부(ON/OFF)를 변경합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<String> toggleAlarm(
            @RequestParam("userId") Long userId,
            @RequestParam("isAlarmOn") Boolean isAlarmOn
    );

    @Operation(
            summary = "FCM 토큰 갱신 API",
            description = "로그인 시 또는 앱 실행 시 FCM 토큰을 갱신합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<String> updateFcmToken(
            @RequestParam("userId") Long userId,
            @RequestParam("fcmToken") String fcmToken
    );
}