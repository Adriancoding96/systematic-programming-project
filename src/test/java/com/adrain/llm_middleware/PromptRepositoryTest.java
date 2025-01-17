package com.adrain.llm_middleware;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.repository.PromptRepository;
import com.adrain.llm_middleware.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


/**
 * PromptRepositoryTest is a test class to verify the correct functionality
 * of {@link PromptRepository} querys.
 *
 * */
@DataJpaTest
@ActiveProfiles("test")
public class PromptRepositoryTest {

    @Autowired
    private PromptRepository  promptRepository;

    @Autowired
    private UserRepository userRepository;  

    /**
     * Tests that {@link Prompt} can be fetched via {@link PromptRepository#findById(Long)}
     * <p>
     *    Verifies that:
     * </p>
     * <ul>
     *   <li>The retieved {@code Prompt} is present.</li>
     *   <li>The {@code Prompt} text matches the one that was saved.</li>
     * </ul>
     * */
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
 
    /**
     * Tests that all {@link Prompt} entities can be fetched via {@link PromptRepository#findAll()}.
     * <p>
     *    Verifies that:
     * </p>
     * <ul>
     *   <li>The list of {@code Prompt} is not empty.</li>
     *   <li>The returned list has the expected number of {@code Prompt} entities.</li>
     * </ul>
     */
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

    /**
     * Tests that all {@link Prompt} entities associated with a user email can be fetched 
     * via {@link PromptRepository#findAllByUserEmail(String)}.
     * <p>
     *    Verifies that:
     * </p>
     * <ul>
     *   <li>The list of {@code Prompt} is not empty.</li>
     *   <li>The returned list has the expected number of {@code Prompt} entities for the given user.</li>
     * </ul>
     */
    @Test
    public void findAllByUserEmail() {
      User user = new User();
      user.setName("Adrian");
      user.setEmail("adrian@example.com");
      user.setPassword("verysecurepassword123");

      userRepository.save(user);


      Prompt prompt1 = new Prompt();
      prompt1.setPrompt("How do i center a div in html");
      prompt1.setResponse(null);
      prompt1.setUser(user);

      Prompt prompt2 = new Prompt();
      prompt2.setPrompt("How do i java in java");
      prompt2.setResponse(null);
      prompt2.setUser(user);

      promptRepository.saveAll(List.of(prompt1, prompt2));

      List<Prompt> prompts = promptRepository.findAllByUserEmail("adrian@example.com");
      assertThat(prompts).isNotEmpty();
      assertThat(prompts).hasSize(2);

      
    }

    /**
     * Tests that a {@link Prompt} entity can be saved via {@link PromptRepository#save(Object)}.
     * <p>
     *    Verifies that:
     * </p>
     * <ul>
     *   <li>The saved {@code Prompt} has a non-null {@code id}.</li>
     *   <li>The {@code Prompt} text matches the one that was set prior to saving.</li>
     * </ul>
     */
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
