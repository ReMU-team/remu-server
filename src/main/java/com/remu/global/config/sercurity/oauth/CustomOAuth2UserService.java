package com.remu.global.config.sercurity.oauth;

import com.remu.domain.user.entity.User;
import com.remu.domain.user.enums.Role;
import com.remu.domain.user.enums.SocialType;
import com.remu.domain.user.repository.UserRepository;
import com.remu.global.config.sercurity.oauth.exception.OAuthException;
import com.remu.global.config.sercurity.oauth.exception.code.OAuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 1. 지원하지 않는 소셜 로그인 체크
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType;
        try {
            socialType = SocialType.valueOf(registrationId.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new OAuthException(OAuthErrorCode.NOT_SUPPORTED_OAUTH);
        }

        // 2. 소셜 서비스 별로 추출할 데이터 필드
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String socialId = "";
        String email = "";
        String name = "";
        String picture = "";

        try {
            // 3. 소셜 서비스 별 데이터 추출
            /* ===================== 구글 ===================== */
            if (socialType == SocialType.GOOGLE) {
                socialId = getString(attributes.get("sub"));
                if (!StringUtils.hasText(socialId)) {
                    socialId = getString(attributes.get("id"));
                }
                email = getString(attributes.get("email"));
                name = getString(attributes.get("name"));
                picture = getString(attributes.get("picture"));
            }

            /* ===================== 카카오 ===================== */
            else if (socialType == SocialType.KAKAO) {
                socialId = getString(attributes.get("id"));
                Map<String, Object> kakaoAccount = asMap(attributes.get("kakao_account"));
                if (kakaoAccount == null) {
                    throw new OAuthException(OAuthErrorCode.OAUTH_RESPONSE_MAPPING_FAILED);
                }

                Map<String, Object> profile = asMap(kakaoAccount.get("profile"));
                if (profile == null) {
                    throw new OAuthException(OAuthErrorCode.OAUTH_RESPONSE_MAPPING_FAILED);
                }

                email = getString(kakaoAccount.get("email"));
                name = getString(profile.get("nickname"));
                picture = getString(profile.get("profile_image_url"));
            }

            /* ===================== 애플 ===================== */
            else if (socialType == SocialType.APPLE) {
                socialId = getString(attributes.get("sub"));
                email = getString(attributes.get("email"));
                name = "Apple User";
            }
        } catch (OAuthException e) {
            // 위에서 던진 exception
            throw e;
        } catch (Exception e) {
            // 구조가 바뀌었거나 캐스팅 실패 시
            throw new OAuthException(OAuthErrorCode.OAUTH_RESPONSE_MAPPING_FAILED);
        }

        // 필수 식별자 검증
        if (!StringUtils.hasText(socialId) || "null".equals(socialId)) {
            throw new OAuthException(OAuthErrorCode.OAUTH_RESPONSE_MISSING_ID);
        }

        // 4. DB 저장 및 업데이트
        String finalSocialId = socialId;
        String finalEmail = email;
        String finalName = name;
        String finalPicture = picture;

        boolean isNewUser;
        User user;

        var optionalUser =
                userRepository.findBySocialTypeAndSocialId(socialType, finalSocialId);

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            isNewUser = false;
        } else {
            user = userRepository.save(
                    User.builder()
                            .socialType(socialType)
                            .socialId(finalSocialId)
                            .email(finalEmail)
                            .name(finalName)
                            .role(Role.USER)
                            .imageUrl(finalPicture)
                            .build()
            );
            isNewUser = true;
        }

        // 5. security 내부에서 사용할 유저 객체 생성
        return new UserPrincipal(user, attributes, isNewUser);
    }

    private static String getString(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    // 캐스팅 가능 여부 검사 + 변환
    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(Object value) {
        // value가 Map인지 instanceof로 확인
        if (value instanceof Map<?, ?>) {
            return (Map<String, Object>) value;
        }
        return null;
    }
}
