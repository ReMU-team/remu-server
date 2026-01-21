package com.remu.domain.emoji.dto.response;

import java.util.List;

public class EmojiResDTO {
    public record Info(
            Long id,
            String imageUrl
    ) {}

    public record ListDTO(
            List<Info> emojis
    ) {}
}
