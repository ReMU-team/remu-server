package com.remu.domain.place.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "place")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 구글Maps api 에 따른 필드값 적용
    @Column(name = "google_place_id")
    private String googlePlaceId;

    @Column(name = "name")
    private String name;
}
