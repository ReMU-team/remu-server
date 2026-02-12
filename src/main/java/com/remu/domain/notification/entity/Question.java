package com.remu.domain.notification.entity;

import com.remu.domain.notification.enums.QuestionDifficulty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content; // 질문 내용

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    @Builder.Default
    private QuestionDifficulty difficulty = QuestionDifficulty.EASY; // 난이도 (기본값 EASY)
}