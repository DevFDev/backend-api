package com.backend.devfordev;

import com.backend.devfordev.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name ="Test Controller")
@Slf4j
@RestController
public class TestController {
    @Operation(summary = "Post Test")
    @PostMapping("/test")
    public ApiResponse<String> Test(@RequestParam String test) {
        try {
            // "test" 요청 파라미터만 사용
            return ApiResponse.onSuccess("Received test parameter: " + test);

        } catch (Exception e) {
            // 예외 발생 시 ApiResponse로 에러 메시지 반환
            throw new RuntimeException(e);
        }
    }
}