package com.adrain.llm_middleware;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.repository.PromptRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PromptRepositoryTest {

    @Autowired
    private PromptRepository  promptRepository;

    @Test
    public void testFindById() {
        Prompt prompt = new Prompt();
        prompt.setPrompt("How do i center a div in html");
        prompt.setResponse(null);

        Prompt savedPrompt = promptRepository.save(prompt);

        Optional<Prompt> optionalPrompt = promptRepository.findById(savedPrompt.getId());

        assertThat(optionalPrompt).isPresent();
        assertThat(optionalPrompt.get().getPrompt()).isEqualTo("How do i center a div in html");
    }

    @Test
    public void findAll() {
        Prompt prompt1 = new Prompt();
        prompt1.setPrompt("How do i center a div in html");
        prompt1.setResponse(null);

        promptRepository.save(prompt1);

        Prompt prompt2 = new Prompt();
        prompt2.setPrompt("How do i deep copy a struct in Rust");
        prompt2.setResponse(null);

        promptRepository.save(prompt2);

        List<Prompt> prompts = promptRepository.findAll();
        assertThat(prompts).isNotEmpty();
        assertThat(prompts).hasSize(2);
    }

    @Test
    public void testSave() {
        Prompt prompt = new Prompt();
        prompt.setPrompt("How do i deep copy a struct in Rust");
        prompt.setResponse(null);

        Prompt savedPrompt = promptRepository.save(prompt);

        assertThat(savedPrompt.getId()).isNotNull();
        assertThat(savedPrompt.getPrompt()).isEqualTo("How do i deep copy a struct in Rust");
    }
}
