package com.remu.domain.resolution.entity;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.review.entity.Review;
import com.remu.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name="resolution_card")
public class Resolution extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "galaxy_id", nullable = false)
    private Galaxy galaxy;

    @OneToOne(mappedBy = "resolution", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Review review;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    public void setReview(Review review) {
        this.review = review;

        // 1. 무한 루프 방지: 이미 연결되어 있다면 종료
        // 2. 상대방(Review)에게도 나(Resolution)를 알려줌으로써 양방향 일치시킴
        if (review != null && review.getResolution() != this) {
            review.setResolution(this);
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
