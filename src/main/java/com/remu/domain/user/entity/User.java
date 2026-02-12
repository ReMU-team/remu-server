package com.remu.domain.user.entity;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.user.enums.Role;
import com.remu.domain.user.enums.SocialType;
import com.remu.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
                ),
                @UniqueConstraint(
                        name = "uk_user_name",
                        columnNames = {"name"}
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

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "name", length = 15)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role; // Role 필드 복구

    @Column(name = "fcm_token")
    private String fcmToken; // FCM 토큰

    @Column(name = "is_alarm_on", nullable = false)
    @Builder.Default
    private Boolean isAlarmOn = true; // 알림 수신 여부 (기본값 ON)

    // 연관 관계
    // 갤럭시 리스트
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Galaxy> galaxies = new ArrayList<>();

    public void updateName(String name) {
        this.name = name;
    }

    public void updateFileName(String fileName) {
        this.fileName = fileName;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    // FCM 토큰 업데이트 메서드
    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    // 알림 설정 변경 메서드
    public void toggleAlarm(Boolean isAlarmOn) {
        this.isAlarmOn = isAlarmOn;
    }

    // Role Key 반환 메서드 (Security에서 사용)
    public String getRoleKey() {
        return this.role.getKey();
    }
}