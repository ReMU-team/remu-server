package com.remu.domain.feedback.service;

import com.remu.domain.feedback.converter.AiFeedbackConverter;
import com.remu.domain.feedback.dto.response.AiFeedbackResDTO;
import com.remu.domain.feedback.entity.AiFeedback;
import com.remu.domain.feedback.exception.AiFeedbackException;
import com.remu.domain.feedback.exception.code.AiFeedbackErrorCode;
import com.remu.domain.feedback.repository.AiFeedbackRepository;
import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.galaxy.exception.code.GalaxyErrorCode;
import com.remu.domain.galaxy.repository.GalaxyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AiFeedbackServiceImpl implements AiFeedbackService{

    private final ChatClient chatClient;
    private final AiFeedbackRepository aiFeedbackRepository;
    private final GalaxyRepository galaxyRepository;

    @Override
    public AiFeedbackResDTO.AiFeedbackCreateDTO createFeedback(
            Long galaxyId
    ) {
        // 1. 은하 찾기
        Galaxy galaxy = galaxyRepository.findByIdWithAllDetails(galaxyId)
                .orElseThrow(() -> new AiFeedbackException(GalaxyErrorCode.GALAXY_NOT_FOUND));

        // 2. 사용자 정보 추출
        String combinedInfo = buildPromptContext(galaxy);

        // 2. 피드백 존재 여부 검증
        if (aiFeedbackRepository.existsByGalaxy(galaxy)) {
            throw new AiFeedbackException(AiFeedbackErrorCode.ALREADY_EXISTS);
        }

        // 3. AI 호츌(String으로 결과 반환)
        String aiResponse = chatClient.prompt()
                .system(s -> s.text("너는 유저의 기록을 분석해주는 다정한 가이드야. " +
                        "제공된 정보를 바탕으로 유저의 성장을 따뜻하게 격려해줘. " +
                        "답변은 3 문장으로, 존댓말로 해줘."))
                .user(combinedInfo)
                .call()
                .content();

        // 4. AI 응답이 없을 경우
        if (aiResponse == null || aiResponse.isBlank()) {
            aiResponse = "유저님의 소중한 여행 기록을 잘 읽어보았어요. 정말 멋진 여정을 보내셨네요! 앞으로의 발걸음도 제가 응원할게요.";
        }

        // 5. 저장 및 후처리
        AiFeedback feedback = aiFeedbackRepository.save(
                AiFeedback.builder()
                        .content(aiResponse)
                        .galaxy(galaxy)
                        .build()
        );

        // 6. 응답 반환
        return AiFeedbackConverter.toCreateDTO(feedback);
    }

    @Transactional(readOnly = true)
    @Override
    public AiFeedbackResDTO.AiFeedbackCreateDTO readFeedback(
            Long galaxyId
    ) {
        // 1. 은하의 존재 여부 확인
        if (!galaxyRepository.existsById(galaxyId)) {
            throw new AiFeedbackException(GalaxyErrorCode.GALAXY_NOT_FOUND);
        }

        AiFeedback feedback = aiFeedbackRepository.findByGalaxyId(galaxyId)
                .orElseThrow(() -> new AiFeedbackException(AiFeedbackErrorCode.NOT_FOUND));

        return AiFeedbackConverter.toCreateDTO(feedback);
    }

    @Override
    public AiFeedbackResDTO.AiFeedbackUpdateDTO updateFeedback(
            Long galaxyId
    ) {
        // 1. 은하 찾기
        Galaxy galaxy = galaxyRepository.findByIdWithAllDetails(galaxyId)
                .orElseThrow(() -> new AiFeedbackException(GalaxyErrorCode.GALAXY_NOT_FOUND));

        // 2. 사용자 정보 추출
        String combinedInfo = buildPromptContext(galaxy);

        // 2. 피드백 존재 여부 검증, 이번엔 기존 내용 업데이트라 없으면 에러
        AiFeedback existingFeedback = galaxy.getAiFeedback();
        if (existingFeedback == null) {
            throw new AiFeedbackException(AiFeedbackErrorCode.NOT_FOUND);
        }

        // 3. AI 호츌(String으로 결과 반환)
        String aiResponse = chatClient.prompt()
                .system(s -> s.text("너는 유저의 기록을 분석해주는 다정한 가이드야. " +
                        "제공된 정보 중 '이전 AI 피드백 내용'을 참고해서, " +
                        "제공된 정보를 바탕으로 유저의 성장을 따뜻하게 격려해줘. " +
                        "답변은 3 문장으로, 존댓말로 해줘."))
                .user(combinedInfo)
                .call()
                .content();

        // 4. AI 응답이 없을 경우
        if (aiResponse == null || aiResponse.isBlank()) {
            aiResponse = "유저님의 소중한 여행 기록을 잘 읽어보았어요. 정말 멋진 여정을 보내셨네요! 앞으로의 발걸음도 제가 응원할게요.";
        }

        // 5. 저장 및 후처리
        existingFeedback.updateContent(aiResponse);

        // 6. 응답 반환
        return AiFeedbackConverter.toUpdateDTO(existingFeedback);

    }

    private String buildPromptContext(Galaxy galaxy) {
        StringBuilder sb = new StringBuilder();

        // [update api] 이전 피드백 맥락
        AiFeedback existingFeedback = galaxy.getAiFeedback();

        if (existingFeedback != null) {
            sb.append("### [참고] 이전 AI 피드백 내용\n")
                    .append("- 지난변 답변: ").append(existingFeedback.getContent()).append("\n")
                    .append("- 이번에는 지난번 대화 내용을 기억하며 유저의 성장을 언급해줘.\n\n");
        }

        // 1. 은하 정보
        sb.append("### 1. 은하 기본 정보\n")
                .append("- 은하 이름: ").append(galaxy.getName()).append("\n")
                .append("- 전체 후기: ").append(galaxy.getReflection() != null ? galaxy.getReflection() : "없음").append("\n\n");

        // 2. 별 정보
        sb.append("### 2. 생성된 별들 (유저의 기억에 남는 순간들)\n");
        if (galaxy.getStars().isEmpty()) {
            sb.append("- 아직 생성된 별이 없습니다.\n");
        } else {
            galaxy.getStars().forEach(star -> {
                sb.append("- 별 제목: ").append(star.getTitle()).append("\n")
                        .append("  내용: ").append(star.getContent()).append("\n");
            });
        }
        sb.append("\n");

        // 3. 다짐 및 회고 (가장 중요한 분석 데이터)
        sb.append("### 3. 유저의 다짐과 실천 기록\n");
        if (galaxy.getResolutions().isEmpty()) {
            sb.append("- 기록된 다짐이 없습니다.\n");
        } else {
            galaxy.getResolutions().forEach(resolution -> {
                sb.append("-> 다짐 내용: ").append(resolution.getContent()).append("\n");

                if (resolution.getReview() == null) {
                    sb.append("  [회고]: 아직 작성된 회고가 없습니다.\n");
                } else {
                    String status = Boolean.TRUE.equals(resolution.getReview().getIsResolutionFulfilled()) ? "성공" : "진행중/미흡";
                    sb.append("  [회고]: ").append(resolution.getReview().getContent()).append("\n")
                            .append("  [다짐의 성취 여부]: ").append(status).append("\n");
                }
            });
        }

        return sb.toString();
    }
}
