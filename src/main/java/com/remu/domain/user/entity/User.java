package com.remu.domain.user.entity;

import com.remu.domain.user.enums.SocialType;
import com.remu.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_social",
                        columnNames = {"social_type", "social_id"}
                )
        }
)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    @Column(name = "social_id", nullable = false)
    private String socialId;

    @Column(name = "email")
    private String email;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "name", length = 15)
    private String name;
}
