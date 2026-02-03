package com.remu.domain.notification.repository;

import com.remu.domain.notification.entity.Question;
import com.remu.domain.notification.enums.QuestionDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    // 랜덤으로 질문 하나 조회 (MySQL 기준)
    @Query(value = "SELECT * FROM question ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Question> findRandomQuestion();

    // 난이도별 랜덤 질문 조회
    @Query(value = "SELECT * FROM question WHERE difficulty = :#{#difficulty.name()} ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Question> findRandomQuestionByDifficulty(@Param("difficulty") QuestionDifficulty difficulty);
}