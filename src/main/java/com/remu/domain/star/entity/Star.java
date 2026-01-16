package com.remu.domain.star.entity;

import com.remu.domain.emoji.entity.Emoji;
import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "star")
public class Star extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title; // 제목

    @Column(name = "content", columnDefinition = "TEXT")
    private String content; // 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color")
    private Emoji color; // 카드 색상

    @Column(name = "image_url")
    private String imageUrl; // 사진

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "galaxy_id", nullable = false)
    private Galaxy galaxy; // 은하 식별자

    // 별과 이모지의 연관관계 (최대 3개 선택 가능)
    @OneToMany(mappedBy = "star", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StarEmoji> starEmojiList = new ArrayList<>();
}