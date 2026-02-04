package com.remu.domain.star.repository;

import com.remu.domain.star.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StarRepository extends JpaRepository<Star, Long> {

    // 1. 상세 조회: 별과 속한 은하(galaxy)를 한 번에 조회 (이모지는 ElementCollection이라 별도 로딩됨)
    @Query("select s from Star s " +
            "join fetch s.galaxy " +
            "where s.id = :starId")
    Optional<Star> findByIdFetchJoin(@Param("starId") Long starId);

    // 2. 목록 조회: 특정 은하의 모든 별 조회
    @Query("select s from Star s " +
            "where s.galaxy.id = :galaxyId " +
            "order by s.recordDate asc")
    List<Star> findAllByGalaxyId(@Param("galaxyId") Long galaxyId);

    // 3. 특정 날짜의 별 개수 조회 (알림 발송 조건 체크용)
    Long countByGalaxyIdAndRecordDate(Long galaxyId, LocalDate recordDate);
}