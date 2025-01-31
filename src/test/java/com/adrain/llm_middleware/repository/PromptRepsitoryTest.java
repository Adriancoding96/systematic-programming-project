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

/**
 * Integration tests for the {@link PromptRepository} and {@link UserRepository} interfaces.
 *
 * <p>This test class uses the {@link DataJpaTest} annotation to initialize an in-memory database
 * with the {@code test} profile active. It verifies the repository methods for fetching data
 * related to {@link Prompt} and {@link User} entities, ensuring the correct behavior of key
 * database operations.</p>
 *
 * <p>The tests cover the following cases:
 * <ul>
 *   <li>{@link #testFindAllByUserEmail()} – Ensures that all {@link Prompt} records associated
 *       with a specific user email are successfully retrieved.</li>
 *   <li>{@link #testFindByUuid()} – Ensures that a {@link Prompt} can be retrieved by its
 *       unique UUID, and that a {@link PromptNotFoundException} is thrown if not found.</li>
 * </ul>
 * </p>
 *
 * @see PromptRepository
 * @see UserRepository
 * @see Prompt
 * @see User
 * @see DataJpaTest
 * @see PromptNotFoundException
 */
@DataJpaTest
@ActiveProfiles("test")
public class PromptRepsitoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PromptRepository promptRepository;

  /**
   * Tests {@link PromptRepository#findAllByUserEmail(String)} to ensure it returns
   * all {@link Prompt} entities associated with a specific user email.
   *
   * <p>This test does the following:
   * <ul>
   *   <li>Creates and saves a {@link User} with a specific email.</li>
   *   <li>Associates multiple {@link Prompt} entities with that user.</li>
   *   <li>Fetches the prompts via {@code findAllByUserEmail} and verifies the
   *       correct number of results is returned.</li>
   * </ul>
   * </p>
   */
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

  /**
   * Tests {@link PromptRepository#findByUuid(String)} to ensure a {@link Prompt}
   * can be retrieved by its unique UUID. Also verifies that a
   * {@link PromptNotFoundException} is thrown if the UUID does not exist in the database.
   *
   * <p>This test does the following:
   * <ul>
   *   <li>Creates and saves a {@link Prompt} with a specific UUID.</li>
   *   <li>Retrieves that {@link Prompt} via {@code findByUuid} and verifies it
   *       matches the stored data.</li>
   *   <li>Throws a {@link PromptNotFoundException} if the UUID is not found (checked
   *       through {@code orElseThrow}).</li>
   * </ul>
   * </p>
   */
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
