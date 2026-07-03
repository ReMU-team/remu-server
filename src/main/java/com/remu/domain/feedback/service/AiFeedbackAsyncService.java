package com.remu.domain.feedback.service;

import com.remu.domain.feedback.entity.AiFeedback;
import com.remu.domain.feedback.repository.AiFeedbackRepository;
import com.remu.domain.galaxy.repository.GalaxyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiFeedbackAsyncService {

    private final ChatClient chatClient;
    private final AiFeedbackRepository aiFeedbackRepository;
    private final GalaxyRepository galaxyRepository;

    @Async("externalApiExecutor")
    @Transactional
    public void executeAiCallAndSave(Long feedbackId, String combinedInfo) {
        try {
            // 외부 API 호출
            String aiResponse = chatClient.prompt()
                    .system(s -> s.text("너는 유저의 기록을 분석해주는 다정한 가이드야. " +
                            "제공된 정보를 바탕으로 유저의 성장을 따뜻하게 격려해줘. " +
                            "답변은 3 문장으로, 존댓말로 해줘."))
                    .user(combinedInfo)
                    .call()
                    .content();

            AiFeedback feedback = aiFeedbackRepository.findById(feedbackId).orElse(null);
            if (feedback == null) return;

            feedback.completeAnalysis(aiResponse);
        } catch (Exception e) {
            // 에러 발생 시 유저의 재시도를 위해 레코드 삭제
            aiFeedbackRepository.deleteById(feedbackId);
        }
    }
}
