package com.remu.domain.galaxy.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.remu.domain.galaxy.entity.Galaxy;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.remu.domain.galaxy.entity.QGalaxy.*;
import static com.remu.domain.resolution.entity.QResolution.*;
import static com.remu.domain.review.entity.QReview.*;
import static com.remu.domain.star.entity.QStar.*;

@RequiredArgsConstructor
public class GalaxyQueryDslImpl implements GalaxyQueryDsl {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Galaxy> findByIdWithAllDetails(Long galaxyId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(galaxy)
                        .leftJoin(galaxy.stars, star)
                        .leftJoin(galaxy.resolutions, resolution).fetchJoin()
                        .leftJoin(resolution.review, review).fetchJoin()
                        .where(galaxy.id.eq(galaxyId))
                        .distinct()
                        .fetchOne()
        );
    }


}
