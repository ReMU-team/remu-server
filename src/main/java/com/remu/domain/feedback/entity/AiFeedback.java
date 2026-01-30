package com.remu.domain.feedback.entity;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "ai_feedback")
public class AiFeedback extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "galaxy_id", nullable = false)
    private Galaxy galaxy;
}
