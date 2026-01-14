package com.remu.domain.galaxy.entity;

import com.remu.domain.emoji.entity.Emoji;
import com.remu.domain.galaxy.enums.GalaxyStatus;
import com.remu.domain.user.entity.User;
import com.remu.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    // 은하를 상징하는 대표 이모지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emoji_id", nullable = false)
    private Emoji emoji;

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
