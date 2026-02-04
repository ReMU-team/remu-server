package com.remu.domain.place.service;

import com.remu.domain.place.dto.response.PlaceResDTO;
import com.remu.domain.place.exception.PlaceException;
import com.remu.domain.place.exception.code.PlaceErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceQueryService {
    @Value("${google.api.key}")
    private String apiKey;

    private final RestClient googleRestClient;

    public PlaceResDTO.PlaceSearchResDTO searchPlace(String query) {
        // 1. 자음/모음만 있는 경우 사전에 차단 (구글에 굳이 보낼 필요 없음)
        if (query == null || query.isBlank() || query.matches("^[ㄱ-ㅎㅏ-ㅣ]+$")) {
            throw new PlaceException(PlaceErrorCode.INVALID_PLACE_QUERY);
        }
        try {
            PlaceResDTO.PlaceSearchResDTO response=googleRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/maps/api/place/autocomplete/json")
                            .queryParam("input", query)
                            .queryParam("types","(regions)")
                            .queryParam("key", apiKey)
                            .queryParam("language", "ko")
                            .build())
                    .retrieve()
                    .body(PlaceResDTO.PlaceSearchResDTO.class);

            if (response==null || "ZERO_RESULTS".equals(response.status())) {
                throw new PlaceException(PlaceErrorCode.PLACE_NOT_FOUND);
            }
            return response;
        }catch (Exception e) {
            // 구글 API 호출 중 발생하는 네트워크 에러 등 처리
            throw new PlaceException(PlaceErrorCode.GOOGLE_API_EXCEPTION);
        }

    }
}
