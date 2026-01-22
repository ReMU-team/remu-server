package com.remu.domain.galaxy.entity;

import com.remu.domain.galaxy.enums.GalaxyStatus;
import com.remu.domain.resolution.entity.Resolution;
import com.remu.domain.star.entity.Star;
import com.remu.domain.user.entity.User;
import com.remu.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "galaxy")
public class Galaxy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    // 이모지
    // 1. 은하를 상징하는 대표 이모지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "galaxy_emoji_id", nullable = false)
    private Emoji galaxyEmoji;

    // 2. 다짐 대표하는 이모지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolution_emoji_id")
    private Emoji resolutionEmoji;

    // 3. 회고 대표하는 이모지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_emoji_id")
    private Emoji reviewEmoji;

    // 연관관계(컬렉션)
    // 다짐 카드 리스트
    @OneToMany(mappedBy = "galaxy", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Resolution> resolutions = new ArrayList<>();

    // 별 리스트
    @OneToMany(mappedBy = "galaxy", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Star> stars = new ArrayList<>();

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status")
    private GalaxyStatus status = GalaxyStatus.READY;

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    private LocalDateTime aiAnalyzedAt;


}
