package com.remu.domain.star.entity;

import com.remu.domain.emoji.entity.Emoji;
import com.remu.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "star_emoji")
public class StarEmoji extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "star_id", nullable = false)
    private Star star;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emoji_id", nullable = false)
    private Emoji emoji;
}