package com.remu.domain.review.entity;

import com.remu.domain.resolution.entity.Resolution;
import com.remu.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "review_card")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "review", fetch = FetchType.LAZY)
    private Resolution resolution;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_resolution_fulfilled")
    private Boolean isResolutionFulfilled;
}
