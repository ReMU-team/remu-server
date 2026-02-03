package com.remu.domain.galaxy.entity;

import com.remu.domain.feedback.entity.AiFeedback;
import com.remu.domain.galaxy.enums.GalaxyStatus;
import com.remu.domain.place.entity.Place;
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
@Setter
@Table(name = "galaxy")
public class Galaxy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "emoji_resource_name", nullable = false)
    private String emojiResourceName;

    // 연관관계(컬렉션)
    // 다짐 카드 리스트
    @OneToMany(mappedBy = "galaxy", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Resolution> resolutions = new ArrayList<>();

    // 별 리스트
    @OneToMany(mappedBy = "galaxy", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Star> stars = new ArrayList<>();

    // Ai 피드백
    @OneToOne(mappedBy = "galaxy", cascade = CascadeType.ALL, orphanRemoval = true)
    private AiFeedback aiFeedback;

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

    @Column(name = "resolution_emoji_id")
    private String resolutionEmojiId;

    @Column(name = "resolution_illust_id")
    private String resolutionIllustId;

    @Column(name = "review_emoji_id")
    private String reviewEmojiId;

    // 여행 전체 후기
    @Column(columnDefinition = "TEXT")
    private String reflection;

    // updateInfo
    public void updateInfo(String name, String emojiResourceName, LocalDate startDate, LocalDate arrivalDate, LocalDate endDate) {
        this.name = name;
        this.emojiResourceName = emojiResourceName;
        this.startDate = startDate;
        this.arrivalDate = arrivalDate;
        this.endDate = endDate;
    }

    // 다짐 이모지 추가 메서드
    public void updateResolutionEmoji(String emojiId) {
        this.resolutionEmojiId = emojiId;
    }

    // 다짐 일러스트 추가 메서드
    public void updateResolutionIllust(String resolutionIllustId) {
        this.resolutionIllustId = resolutionIllustId;
    }

    // 회고 이모지 추가 메서드
    public void updateReviewEmoji(String emojiId) {
        this.reviewEmojiId = emojiId;
    }

    // 여행 후기 일러스트 추가 메서드
    public void updateReflection(String reflection) {
        this.reflection = reflection;
    }
}
