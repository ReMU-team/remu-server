package com.remu.domain.star.entity;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    @Column(name = "record_date")
    private LocalDate recordDate; // 여행 날짜 (사용자가 선택한 날짜)

    @Column(name = "card_color")
    private String cardColor; // 카드 색상 (String으로 저장)

    @Column(name = "image_url")
    private String imageUrl; // 사진

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "galaxy_id", nullable = false)
    private Galaxy galaxy; // 은하 식별자

    // 별과 이모지의 연관관계 -> 단순 문자열 리스트로 변경
    @ElementCollection
    @CollectionTable(name = "star_emojis", joinColumns = @JoinColumn(name = "star_id"))
    @Column(name = "emoji_name")
    @Builder.Default
    private List<String> emojis = new ArrayList<>();
}