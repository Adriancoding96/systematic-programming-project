package com.adrain.llm_middleware;

import com.adrain.llm_middleware.service.impl.PromptServiceImpl;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PromptServiceTest {

  private final PromptServiceImpl promptServiceImpl;
  
  public PromptServiceTest(PromptServiceImpl promptServiceImpl) {
    this.promptServiceImpl = promptServiceImpl;
  }


}
