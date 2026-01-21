package com.remu.domain.emoji.repository;

import com.remu.domain.emoji.entity.Emoji;
import com.remu.domain.emoji.enums.EmojiType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface EmojiRepository extends JpaRepository<Emoji, Long> {
    List<Emoji> findAllByType(EmojiType type);
}
