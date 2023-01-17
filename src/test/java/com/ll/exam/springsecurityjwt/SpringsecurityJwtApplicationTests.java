package com.ll.exam.springsecurityjwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class SpringsecurityJwtApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	@DisplayName("POST /member/login 은 로그인 처리 URL 이다.")
	void t1() throws Exception {

		// When
		ResultActions resultActions = mvc
				.perform(
						post("/member/login")
								.content("""
                                        {
                                            "username": "user1",
                                            "password": "1234"
                                        }
                                        """.stripIndent())
								.contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		// Then
		resultActions
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	@DisplayName("POST /member/login 으로 올바른 username과 password 데이터를 넘기면 JWT키를 발급해준다.")
	void t2() throws Exception {
		// When
		ResultActions resultActions = mvc
				.perform(
						post("/member/login")
								.content("""
                                        {
                                            "username": "user1",
                                            "password": "1234"
                                        }
                                        """.stripIndent())
								.contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		// Then
		resultActions
				.andExpect(status().is2xxSuccessful());

		MvcResult mvcResult = resultActions.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		String authentication = response.getHeader("Authentication");

		Assertions.assertThat(authentication).isNotEmpty();
	}

	/**
	 * POST /member/login 의 응답 헤더에는 Authentication 값이 있어야 합니다.
	 */
}
