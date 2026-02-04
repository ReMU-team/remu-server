package com.remu.global.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    // ELB 상태 검사용 (/health) API
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        // 상태 코드 200과 함께 메시지 반환
        return ResponseEntity.ok("Success Health Check");
    }
}
