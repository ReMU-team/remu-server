package com.remu.domain.user.controller;

import com.remu.domain.user.dto.req.UserReqDTO;
import com.remu.domain.user.exception.code.UserSuccessCode;
import com.remu.domain.user.service.UserService;
import com.remu.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        UserSuccessCode code = UserSuccessCode.USER_FOUND;
        return ApiResponse.onSuccess(code, userService.updateProfile(userId, dto));
    }

}
