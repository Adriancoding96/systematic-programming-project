package com.adrain.llm_middleware;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.repository.PromptRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PromptRepositoryTest {
  
  @Mock
  private PromptRepository promptRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    Prompt prompt = new Prompt();
    prompt.setId(1L);
    prompt.setPrompt("How do i center a div in html");
    prompt.setResponse(null);

    when(promptRepository.findById(1L)).thenReturn(Optional.of(prompt));
    when(promptRepository.findAll()).thenReturn(Arrays.asList(prompt));
 
    when(promptRepository.save(any(Prompt.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));
  }

  @Test
  public void testFindById() {
    Optional<Prompt> optionalPrompt = promptRepository.findById(1L);
    assertThat(optionalPrompt).isNotEmpty();
    assertThat(optionalPrompt.get().getPrompt()).isEqualTo("How do i center a div in html");
  }

  @Test
  public void findAll() {
    List<Prompt> prompts = promptRepository.findAll();
    assertThat(prompts).isNotEmpty();
    assertThat(prompts).hasSize(1);
  }

  @Test
  public void testSave() {
    Prompt prompt = new Prompt();
    prompt.setPrompt("How do i deep copy a struct in Rust");
    prompt.setResponse(null);
    
    Prompt savedPrompt = promptRepository.save(prompt);
    assertThat(prompt.getPrompt()).isEqualTo("How do i deep copy a struct in Rust");
  }
}
