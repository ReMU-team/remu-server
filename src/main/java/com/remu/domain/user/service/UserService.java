package com.remu.domain.user.service;

import com.remu.domain.user.dto.req.UserReqDTO;
import com.remu.domain.user.dto.res.UserResDTO;
import com.remu.domain.user.entity.User;
import com.remu.domain.user.enums.Role;
import com.remu.domain.user.enums.SocialType;
import com.remu.domain.user.exception.UserException;
import com.remu.domain.user.exception.code.UserErrorCode;
import com.remu.domain.user.repository.UserRepository;
import com.remu.global.auth.service.KakaoAuthService;
import com.remu.global.s3.S3Directory;
import com.remu.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final KakaoAuthService kakaoAuthService;

    // 프로필 업데이트
    @Transactional
    public Void updateProfile(Long userId, UserReqDTO.ProfileDTO dto, MultipartFile image){

        // 1. 유저 존재 여부 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 2. 닉네임 업데이트
        updateName(user, dto.name());

        // 3. 한 줄 소개 업데이트
        updateIntroduction(user, dto.introduction());

        // 4. 프로필 이미지 업로드
        String newFileName = null;
        if (image != null && !image.isEmpty()) {
            S3Service.S3TotalResponse response = s3Service.uploadFile(image, S3Directory.PROFILE, userId);
            newFileName = response.fileName();
        }

        // 5. 이미지가 새로 들어온 경우 교체 + 기존 S3 삭제
        if (newFileName != null) {
            String oldFileName = user.getFileName();

            user.updateFileName(newFileName);

            if (isS3Key(oldFileName)) {
                safeDelete(oldFileName);
            }
        }

        return null;
    }

    // 사용 가능한 닉네임인지 검증
    public UserResDTO.NameCheckDTO checkName(String rawName, Long userId) {

        // 1. 닉네임 값 null 확인
        if (rawName == null || rawName.isBlank()) {
            return UserResDTO.NameCheckDTO.invalid_short();
        }

        // 2. 닉네임 공백 제거
        String name = rawName.trim();

        // 3. 닉네임 2 ~ 15자 확인
        if (name.length() < 2) {
            return UserResDTO.NameCheckDTO.invalid_short();
        }
        else if (name.length() > 15) {
            return UserResDTO.NameCheckDTO.invalid_long();
        }

        // 4. 유저 존재 여부 검증 및 조회
        User me = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 5. 현재 사용자의 닉네임과 동일할 때 valid 반환
        if (name.equals(me.getName())) {
            return UserResDTO.NameCheckDTO.valid();
        }

        boolean exists = userRepository.existsByName(name);

        // 6. 다른 닉네임과 중복되면 duplicate 반환, 중볻되지 않으면 valid 반환
        return exists
                ? UserResDTO.NameCheckDTO.duplicate() : UserResDTO.NameCheckDTO.valid();
    }

    // 프로필 조회
    public UserResDTO.ProfileDTO getProfile(
            Long userId
    ){
        // 1. 유저 존재 여부 검증 및 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 2. 이미지 url 받아오기
        String key = user.getFileName();
        String imageUrl = isS3Key(key) ? s3Service.getPresignedUrl(key) : key;

        // 2. 컨버터를 이용한 DTO 반환
        return UserResDTO.ProfileDTO.builder()
                .name(user.getName())
                .introduction(user.getIntroduction())
                .imageUrl(imageUrl)
                .build();
    }

    // 회원 탈퇴
    @Transactional
    public Void deleteAccount(Long userId) {

        // 1. 유저 존재 여부 검증 및 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 2. 카카오 연결 끊기 API 호출
        if (user.getSocialType() == SocialType.KAKAO) {
            kakaoAuthService.unlinkKakao(user.getSocialId());
        }

        // 3. 유저 삭제 : CascadeType.ALL 설정으로 연관된 엔티티 데이터 자동으로 삭제
        userRepository.delete(user);

        return null;
    }

    public record UserResult(User user, boolean isNewUser) {}

    @Transactional
    public UserResult findOrCreateUser(String socialId, SocialType socialType, String email, String name, String imageUrl) {
        // 1. DB에서 유저를 찾기
        return userRepository.findBySocialTypeAndSocialId(socialType, socialId)
                .map(user -> new UserResult(user, false)) // 있으면 기존 유저 (false)
                .orElseGet(() -> {
                    // 없으면 새로 생성하고 신규 유저 (true) 반환
                    User newUser = userRepository.save(
                            User.builder()
                                    .socialId(socialId)
                                    .socialType(socialType)
                                    .email(email)
                                    .name(name)
                                    .fileName(imageUrl)
                                    .role(Role.USER)
                                    .build()
                    );
                    return new UserResult(newUser, true);
                });
    }

    // 알림 설정 변경 (ON/OFF)
    @Transactional
    public void toggleAlarm(Long userId, Boolean isAlarmOn) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        user.toggleAlarm(isAlarmOn);
    }

    // FCM 토큰 갱신
    @Transactional
    public void updateFcmToken(Long userId, String fcmToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        user.updateFcmToken(fcmToken);
    }

    // 이름 업데이트
    private void updateName(User user, String rawName) {
        if (rawName != null) {
            String name = rawName.trim();
            if(name.length() < 2 || name.length() > 15) {
                throw new UserException(UserErrorCode.INVALID_NAME);
            }

            // 현재 자신의 이름 외에 중복 검사
            if(userRepository.existsByNameAndIdNot(name, user.getId())){
                throw new UserException(UserErrorCode.NAME_DUPLICATE);
            }

            user.updateName(name);
        }
    }

    // S3 키가 맞는지 검증
    private boolean isS3Key(String value) {
        if(value == null || value.isBlank()) return false;
        String v = value.trim();
        return !(v.startsWith("http://") || v.startsWith("https://"));
    }

    // 기존 이미지 삭제
    private void safeDelete(String key) {
        try {
            s3Service.deleteFile(key);
        } catch (Exception ignored) {

        }
    }

    // 한 줄 소개 업데이트
    private void updateIntroduction(User user, String rawIntro) {
        if (rawIntro != null) {
            String intro = rawIntro.trim();
            user.updateIntroduction(
                    intro.isEmpty() ? null : intro
            );
        }
    }
}