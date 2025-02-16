package com.adrain.llm_middleware;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"api.key=TEST_API_KEY"})
@ActiveProfiles("test")
class LlmMiddlewareApplicationTests {

	@Test
	void contextLoads() {
	}

}
