package com.remu.domain.user.controller;

import com.remu.domain.user.dto.req.UserReqDTO;
import com.remu.domain.user.dto.res.UserResDTO;
import com.remu.domain.user.exception.code.UserSuccessCode;
import com.remu.domain.user.service.UserService;
import com.remu.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerDocs{

    private final UserService userService;

    @Override
    @PostMapping("/api/profile")
    public ApiResponse<Void> updateProfile(
            Long userId,
            @RequestBody @Valid UserReqDTO.ProfileDTO dto
    ){
        UserSuccessCode code = UserSuccessCode.USER_PROFILE_CREATED;
        return ApiResponse.onSuccess(code, userService.updateProfile(userId, dto));
    }

    @Override
    @GetMapping("/api/name/exists")
    public UserResDTO.NameCheckDTO checkName(
            @RequestParam(required = false) String name,
            Long userId
    ) {
        return userService.checkName(name, userId);
    }

    @Override
    @GetMapping("/api/profile")
    public ApiResponse<UserResDTO.ProfileDTO> getProfile(
            Long userId
    ) {
        UserSuccessCode code = UserSuccessCode.USER_PROFILE_SEARCH;
        return ApiResponse.onSuccess(code, userService.getProfile(userId));
    }

    @Override
    @DeleteMapping("api/account")
    public ApiResponse<Void> deleteAccount(
            Long userId
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