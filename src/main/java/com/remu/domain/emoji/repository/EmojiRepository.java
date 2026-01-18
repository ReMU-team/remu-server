package com.remu.domain.emoji.repository;

import com.remu.domain.emoji.entity.Emoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmojiRepository extends JpaRepository<Emoji, Long> {
}
