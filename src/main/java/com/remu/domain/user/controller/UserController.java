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
}
