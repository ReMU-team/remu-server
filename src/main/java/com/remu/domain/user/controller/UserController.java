package com.remu.domain.user.controller;

import com.remu.domain.user.dto.req.UserReqDTO;
import com.remu.domain.user.dto.res.UserResDTO;
import com.remu.domain.user.exception.code.UserSuccessCode;
import com.remu.domain.user.service.UserService;
import com.remu.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerDocs{

    private final UserService userService;

    @Override
    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Void> updateProfile(
            @AuthenticationPrincipal(expression = "id") Long userId,

            @RequestPart("name") String name,
            @RequestPart(value = "introduction", required = false) String introduction,

            @Parameter(description = "프로필 이미지 파일", schema = @Schema(type = "string", format = "binary"))
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        UserReqDTO.ProfileDTO dto = new UserReqDTO.ProfileDTO(name, introduction);
        userService.updateProfile(userId, dto, image);
        return ApiResponse.onSuccess(UserSuccessCode.USER_PROFILE_CREATED, null);
    }

    @Override
    @GetMapping("/names/availability")
    public UserResDTO.NameCheckDTO checkName(
            @RequestParam(required = false) String name,
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        return userService.checkName(name, userId);
    }

    @Override
    @GetMapping("/profile")
    public ApiResponse<UserResDTO.ProfileDTO> getProfile(
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        UserSuccessCode code = UserSuccessCode.USER_PROFILE_SEARCH;
        return ApiResponse.onSuccess(code, userService.getProfile(userId));
    }

    @Override
    @DeleteMapping("/account")
    public ApiResponse<Void> deleteAccount(
            @AuthenticationPrincipal(expression = "id") Long userId
    ){
        UserSuccessCode code = UserSuccessCode.USER_DELETE_ACCOUNT;
        return ApiResponse.onSuccess(code, userService.deleteAccount(userId));
    }

    // 알림 설정 변경 (ON/OFF)
    @PatchMapping("/api/v1/users/alarm")
    public ApiResponse<String> toggleAlarm(
            @RequestParam("userId") Long userId,
            @RequestParam("isAlarmOn") Boolean isAlarmOn
    ) {
        userService.toggleAlarm(userId, isAlarmOn);
        return ApiResponse.onSuccess(UserSuccessCode.USER_PROFILE_CREATED, "알림 설정이 변경되었습니다.");
    }

    // FCM 토큰 갱신
    @PatchMapping("/api/v1/users/fcm-token")
    public ApiResponse<String> updateFcmToken(
            @RequestParam("userId") Long userId,
            @RequestParam("fcmToken") String fcmToken
    ) {
        userService.updateFcmToken(userId, fcmToken);
        return ApiResponse.onSuccess(UserSuccessCode.USER_PROFILE_CREATED, "FCM 토큰이 갱신되었습니다.");
    }
}