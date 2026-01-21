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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "review_id")
    private Review review;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    public void updateContent(String content) {
        this.content = content;
    }
}
