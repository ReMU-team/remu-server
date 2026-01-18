package com.remu.domain.place.repository;

import com.remu.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    public Place findByGooglePlaceId(String googlePlaceId);
}
