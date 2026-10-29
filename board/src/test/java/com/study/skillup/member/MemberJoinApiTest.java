package com.study.skillup.member;

import com.study.skillup.common.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 가입 API 자동 시험: 서버를 띄우지 않고 가짜 요청을 필터 → 컨트롤러 → 서비스 → DB까지 흘려 상태 코드·문구를 확인
// @Transactional: 각 시험이 끝나면 DB에 넣은 회원을 되돌림(롤백) → 시험을 몇 번 돌려도 DB에 안 남음
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberJoinApiTest {

    // 가짜 요청을 보내는 도구
    @Autowired
    MockMvc mockMvc;

    // 가입 요청 하나 만들기(CSRF 토큰 포함). 가입 화면의 FormData처럼 칸 세 개를 폼 값으로
    private MockHttpServletRequestBuilder joinRequest(String userId, String pwd, String name) {
        return post("/api/member/join")
                .with(csrf())
                .param("userId", userId)
                .param("pwd", pwd)
                .param("name", name);
    }

    @Test
    @DisplayName("정상 가입이면 201")
    void joinSuccess() throws Exception {
        mockMvc.perform(joinRequest("mvc_test_user", "abc12345", "시험"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("같은 아이디로 두 번 가입하면 두 번째는 409 + 중복 문구")
    void joinDuplicate() throws Exception {
        mockMvc.perform(joinRequest("mvc_test_user", "abc12345", "시험"))
                .andExpect(status().isCreated());

        mockMvc.perform(joinRequest("mvc_test_user", "abc12345", "시험"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(ErrorMessage.USER_ID_DUPLICATE));
    }

    @Test
    @DisplayName("비밀번호 규칙 위반이면 400 + 규칙 문구")
    void joinPwdRule() throws Exception {
        mockMvc.perform(joinRequest("mvc_test_user", "abcdefgh", "시험"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.PWD_RULE_VIOLATION));
    }

    @Test
    @DisplayName("빈 칸이 있으면 400 + 빈 칸 문구")
    void joinBlank() throws Exception {
        mockMvc.perform(joinRequest("mvc_test_user", "abc12345", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.JOIN_BLANK));
    }

    @Test
    @DisplayName("CSRF 토큰이 없으면 403")
    void joinWithoutCsrf() throws Exception {
        mockMvc.perform(post("/api/member/join")
                        .param("userId", "mvc_test_user")
                        .param("pwd", "abc12345")
                        .param("name", "시험"))
                .andExpect(status().isForbidden());
    }
}
