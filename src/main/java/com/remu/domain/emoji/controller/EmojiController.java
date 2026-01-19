package com.remu.domain.emoji.controller;

import com.remu.domain.emoji.dto.response.EmojiResDTO;
import com.remu.domain.emoji.enums.EmojiType;
import com.remu.domain.emoji.exception.code.EmojiSuccessCode;
import com.remu.domain.emoji.service.EmojiService;
import com.remu.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emojis")
public class EmojiController {

    private final EmojiService emojiService;

    // 타입별 이모지 조회
    // GET /emojis?type=GALAXY
    @GetMapping
    public ApiResponse<EmojiResDTO.ListDTO> getEmojis(
            @RequestParam(value = "type") EmojiType type
    ) {
        EmojiResDTO.ListDTO response = emojiService.getEmojisByType(type);
        return ApiResponse.onSuccess(EmojiSuccessCode.EMOJI_FETCH_SUCCESS, response);
    }


}
