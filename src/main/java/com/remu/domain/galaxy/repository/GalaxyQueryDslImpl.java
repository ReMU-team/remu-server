package com.remu.domain.galaxy.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.remu.domain.feedback.entity.QAiFeedback;
import com.remu.domain.galaxy.entity.Galaxy;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.remu.domain.feedback.entity.QAiFeedback.*;
import static com.remu.domain.galaxy.entity.QGalaxy.*;
import static com.remu.domain.resolution.entity.QResolution.*;
import static com.remu.domain.review.entity.QReview.*;
import static com.remu.domain.star.entity.QStar.*;

@RequiredArgsConstructor
public class GalaxyQueryDslImpl implements GalaxyQueryDsl {
    private final JPAQueryFactory queryFactory;

    // 기존 카타시안 곱 문제 발생.
    // fetch join 방식이 아닌 batch size 설정을 통해 최적화
//    @Override
//    public Optional<Galaxy> findByIdWithAllDetails(Long galaxyId) {
//        return Optional.ofNullable(
//                queryFactory
//                        .selectFrom(galaxy)
//                        .leftJoin(galaxy.stars, star)
//                        .leftJoin(galaxy.resolutions, resolution).fetchJoin()
//                        .leftJoin(resolution.review, review).fetchJoin()
//                        .leftJoin(galaxy.aiFeedback, aiFeedback).fetchJoin()
//                        .where(galaxy.id.eq(galaxyId))
//                        .distinct()
//                        .fetchOne()
//        );
//    }

    @Override
    public Optional<Galaxy> findByIdWithAllDetails(Long galaxyId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(galaxy)
                        // 기존 1대1 관계는 중복 발생X
                        .leftJoin(galaxy.aiFeedback, aiFeedback).fetchJoin()
                        // 기존 1대n 엔티티는 fetch join 제거
                        .leftJoin(galaxy.stars, star)
                        .leftJoin(galaxy.resolutions, resolution)
                        .where(galaxy.id.eq(galaxyId))
                        .fetchOne()
        );
    }
}
