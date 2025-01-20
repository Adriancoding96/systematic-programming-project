package com.adrain.llm_middleware;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.util.KeywordMatcher;
import com.adrain.llm_middleware.util.KeywordSearcher;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * KeywordMatcherTest is to verify correct functionality of {@link KeywordMatcher}
 * methods.
 */
@SpringBootTest
@ActiveProfiles("test")
public class KeywordMatcherTest {

  private final KeywordMatcher matcher = new KeywordMatcher();

  /**
   * Tests that the {@link KeywordSearcher#buildWordFrequencyMap} can construct
   * a hashmap containing words and their frequency from a text.
   * <p>
   *    Verifies that:
   * </p>
   * <ul>
   *   <li>The resulting hashmap is equal to the hardcoded example.</li>
   * </ul>
   */
  @Test
  void testBuildWordFrequencyMap() {
    String text = "Hello World, Hello World.";
    Map<String, Integer> expected = Map.of(
      "Hello", 2,
      "World", 2
    );

    Map<String, Integer> result = matcher.buildWordFrequencyMap(text);
    assertEquals(expected, result);
  }

  /**
   * Tests that the {@link KeywordSearcher#calculateSimilarityOfStrings} produces the
   * correct similarity score of two identical strings.
   * <p>
   *    Verifies that:
   * </p>
   * <ul>
   *   <li>The resulting similarity score is >= 0.8</li>
   * </ul>
   */
  @Test
  void testCalculateSimilarityOfEqualStrings() {
    String prompt = "how do i deep copy a struct in rust";
    Map<String, Integer> inputWordFreq = Map.of(
      "how", 1,
      "do", 1,
      "i", 1,
      "deep", 1,
      "copy", 1,
      "a", 1,
      "struct", 1,
      "in", 1,
      "rust", 1
    );

    double similarity = matcher.calculateSimilarity(inputWordFreq, prompt);
    assertTrue(similarity >= 0.8, "Expected similarity to be >= 0,8");
  }  
 
  /**
   * Tests that the {@link KeywordSearcher#calculateSimilarityOfStrings} produces the
   * correct similarity score for two completly different strings.
   * <p>
   *    Verifies that:
   * </p>
   * <ul>
   *   <li>The resulting similarity score is <= 0.8</li>
   * </ul>
   */
  @Test
  void testCalculateSimilarityOfNotEqualStrings() {
    String prompt = "how do i deep copy a struct in rust";
    Map<String, Integer> inputWordFreq = Map.of(
      "how", 1,
      "do", 1,
      "i", 1,
      "center", 1,
      "a", 1,
      "div", 1,
      "in", 1,
      "html", 1,
      "please", 1
    );

    double similarity = matcher.calculateSimilarity(inputWordFreq, prompt);
    assertFalse(similarity >= 0.8, "Expected similarity to be >= 0,8");
  }

  /**
   * Tests that the {@link KeywordSearcher#calculateSimilarityOfStrings} produces the
   * correct similarity score for two closely resembling strings.
   * <p>
   *    Verifies that:
   * </p>
   * <ul>
   *   <li>The resulting similarity score is >= 0.8</li>
   * </ul>
   */
  @Test
  void testCalculateSimilarityOfSimilarStrings() {
    String prompt = "how do i deep copy a struct in rust";
    Map<String, Integer> inputWordFreq = Map.of(
      "how", 1,
      "do", 1,
      "i", 1, // Notice the word deep is not present in this test
      "copy", 1,
      "a", 1,
      "struct", 1,
      "in", 1,
      "rust", 1
    );

    double similarity = matcher.calculateSimilarity(inputWordFreq, prompt);
    assertTrue(similarity >= 0.8, "Expected similarity to be >= 0,8. The similarity was: " + similarity);
  }

  /**
   * Tests that the {@link KeywordSearcher#checkSimilarityOfTextAndStream} returns a {@link Prompt} from a {@code Stream} 
   * that has a similarity score of 0.8 or greater compared to the provided text.
   * <p>
   *    Verifies that:
   * </p>
   * <ul>
   *   <li>The {@link Prompt} result is not {@code null}.</li>
   *   <li>The {@link Prompt} equals the first possble occurance with a similarity score >= 0.8.</li>
   * </ul>
   */
  @Test
  void testCheckSimilarityOfTextAndStream() {
    String prompt = "How do i center a div in html using css";
    List<Prompt> prompts = List.of(
      new Prompt(null, "I like programming in java", null, null),
      new Prompt(null, "How", null, null),
      new Prompt(null, "How do", null, null),
      new Prompt(null, "How do i", null, null),
      new Prompt(null, "How do i center", null, null),
      new Prompt(null, "How do i center a", null, null),
      new Prompt(null, "How do i center a div in", null, null),
      new Prompt(null, "How do i center a div in html", null, null), // This should be present in result as 8 of 10 words match
      new Prompt(null, "How do i center a div in html using", null, null),
      new Prompt(null, "How do i center a div in html using css", null, null)
    );

    Prompt result = matcher.checkSimilarityOfTextAndStream(prompt, prompts.stream());
    assertNotNull(result);
    assertEquals("How do i center a div in html", result.getPrompt());
  }

  /**
   * Tests that the {@link KeywordSearcher#checkSimilarityOfTextAndStream} returns null, as no {@link Prompt},s is
   * equal to similarity score >= 0.8.
   * <p>
   *    Verifies that:
   * </p>
   * <ul>
   *   <li>The {@link Prompt} result is {@code null}.</li>
   * </ul>
   */
  @Test
  void testCheckSimilarityOfTextAndStream_NoMatch() {
    String prompt = "Java is fun!";
    List prompts = List.of(
      new Prompt(null, "How do i center a div in html using css", null, null),
      new Prompt(null, "How do i deep copy a struct in rust", null, null)
    );

    Prompt result = matcher.checkSimilarityOfTextAndStream(prompt, prompts.stream());
    assertNull(result);
  }

  /**
   * Tests the time it takes for {@link KeywordSearcher#checkSimilarityOfTextAndStream}
   * to run with a stream consisting of 100_000 {@link Prompt} objects. Finally asserts
   * result not to be null.
   * <p>
   *    Checks:
   * </p>
   * <ul>
   *   <li>The time of execution with 100_000 {@link Prompt} objects in stream.</li>
   * </ul>
   * <p>
   *    Verifies that:
   * </p>
   * <ul>
   *   <li>The {@link Prompt} result is not {@code null}.</li>
   * </ul>
   */
  @Test
  void testPerformance() {
    String prompt = "How do i program a program";
    List<Prompt> prompts = new ArrayList<>();
    for(int i = 0; i < 100_000; i++) {
      prompts.add(new Prompt(null, "prompt " + i, null, null));
    }
    prompts.add(new Prompt(null, "How do i program a program", null, null));

    long startTime = System.currentTimeMillis();
    Prompt result = matcher.checkSimilarityOfTextAndStream(prompt, prompts.stream());
    long endTime = System.currentTimeMillis();

    System.out.println("Execution time: " + (endTime - startTime) + "ms");
    assertNotNull(result);
  }
}
