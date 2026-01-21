package com.remu.domain.emoji.service;

import com.remu.domain.emoji.dto.response.EmojiResDTO;
import com.remu.domain.emoji.enums.EmojiType;
import com.remu.domain.emoji.repository.EmojiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmojiService {
    private final EmojiRepository emojiRepository;


    public EmojiResDTO.ListDTO getEmojisByType(EmojiType type) {
        List<EmojiResDTO.Info> emojiInfos = emojiRepository.findAllByType(type).stream()
                .map(emoji -> new EmojiResDTO.Info(emoji.getId(), emoji.getImageUrl()))
                .toList();

        return new EmojiResDTO.ListDTO(emojiInfos);
    }
}
