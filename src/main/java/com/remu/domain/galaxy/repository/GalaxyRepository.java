package com.remu.domain.galaxy.repository;

import com.remu.domain.galaxy.entity.Galaxy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GalaxyRepository extends JpaRepository<Galaxy, Long>, GalaxyQueryDsl {
    Slice <Galaxy> findAllByUserId(Long userId, Pageable pageable);

    // 총 개수
    Long countByUserId(Long userId);

    // 1. 여행 도착일이 특정 날짜인 은하 조회 (ARRIVAL, RECORD 알림용)
    List<Galaxy> findAllByArrivalDate(LocalDate arrivalDate);

    // 2. 여행 종료일이 특정 날짜인 은하 조회 (REVIEW 알림용)
    List<Galaxy> findAllByEndDate(LocalDate endDate);

    // 3. 여행 기간 중에 포함되는 은하 조회 (QUESTION 알림용)
    // startDate <= today <= endDate
    @Query("select g from Galaxy g where :today >= g.startDate and :today <= g.endDate")
    List<Galaxy> findAllByDateBetween(@Param("today") LocalDate today);
}