package com.adrain.llm_middleware.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.stream.Collectors;

import com.adrain.llm_middleware.exception.PromptNotFoundException;
import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.model.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class PromptRepsitoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PromptRepository promptRepository;

  @Test
  public void testFindAllByUserEmail() {
    User user = new User();
    user.setName("Adrian");
    user.setEmail("adrian@example.com");
    user.setPassword("verysecurepassword123");

    User savedUser = userRepository.save(user);

    Prompt prompt1 = new Prompt();
    prompt1.setUuid("12345");
    prompt1.setPrompt("How do i html?");
    prompt1.setUser(savedUser);
    
    Prompt prompt2 = new Prompt();
    prompt2.setUuid("54321");
    prompt2.setPrompt("How do i not cause compilation errors in rust");
    prompt2.setUser(savedUser);

    promptRepository.saveAll(List.of(prompt1, prompt2));

    List<Prompt> prompts = promptRepository.findAllByUserEmail("adrian@example.com")
      .collect(Collectors.toList());

      assertThat(prompts).hasSize(2);
  }

  @Test
  public void testFindByUuid() {
    String uuid = "wow-so-unique";

    Prompt prompt = new Prompt();
    prompt.setUuid(uuid);
    prompt.setPrompt("How do i html?");
    prompt.setUser(null);

    promptRepository.save(prompt);
    
    Prompt fetchedPrompt = promptRepository.findByUuid(uuid)
      .orElseThrow(() -> new PromptNotFoundException("Prompt not found in database with uuid: " + uuid));

    assertNotNull(fetchedPrompt);
    assertEquals("How do i html?", fetchedPrompt.getPrompt());
    assertEquals(uuid, fetchedPrompt.getUuid());
  }

}
