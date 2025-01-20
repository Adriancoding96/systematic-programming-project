package com.adrain.llm_middleware.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.adrain.llm_middleware.model.Prompt;

import org.springframework.stereotype.Component;

/**
 * KeywordMatcher is an component for comparing text similarity and building word frequency maps.
 * <p>
 *     It uses regular expressions to analyze text and calculate similarity scores between
 *     a given input text and a collection of {@link Prompt} objects.
 * </p>
 */
@Component
public class KeywordMatcher {

  private final String REGEX = "[a-zA-Z0-9]+";
  private Pattern pattern;

  public KeywordMatcher() {
    pattern = Pattern.compile(REGEX);
  }
  
  /**
   * Checks similarity between an input text and a stream of {@link Prompt} objects.
   * <p>
   *     Compares the input text against each {@link Prompt} in the stream by calculating a similarity
   *     score. If the similarity is >= to 0.8 the matching {@link Prompt} is returned.
   * </p>
   *
   * @param text The input text to compare.
   * @param promptRows A stream of {@link Prompt} objects to compare against.
   * @return The first {@link Prompt} object with a similarity >= 0.8 or {@code null} if no match is found.
   */
  public Prompt checkSimilarityOfTextAndStream(String text, Stream<Prompt> promptRows) {
    Map<String, Integer> inputWordFreq = buildWordFrequencyMap(text);

    return promptRows.filter(prompt -> {
      String promptText = prompt.getPrompt();
      return calculateSimilarity(inputWordFreq, promptText) >= 0.8;
    }).findFirst().orElse(null);
  }

  public Map<String, Integer> buildWordFrequencyMap(String text) {
    Map<String, Integer> wordFreq = new HashMap<>();
    Matcher matcher = pattern.matcher(text);
    while(matcher.find()) {
      String word = matcher.group();
      wordFreq.put(word, wordFreq.getOrDefault(word, 0) + 1);
    }
    return wordFreq;
  }
 
  /**
   * Builds a word frequency map from a given text.
   * <p>
   *     Extracts words matching the regex pattern and counts their occurrences in the text.
   * </p>
   *
   * @param text The text to process.
   * @return A {@link Map} where keys are words and values are their respective frequencies.
   */
  public double calculateSimilarity(Map<String, Integer> inputWordFreq, String prompt) {
    Map<String, Integer> promptWordFreq = new HashMap<>();
    Matcher matcher = pattern.matcher(prompt);

    int intersectionCount = 0;
    int totalWords = 0;

    while(matcher.find()) {
      String word = matcher.group();
      promptWordFreq.put(word, promptWordFreq.getOrDefault(word, 0) + 1);
      totalWords++;
      if(inputWordFreq.containsKey(word) && promptWordFreq.get(word) <= inputWordFreq.get(word)) {
        intersectionCount++;
      }
    }

    totalWords += inputWordFreq.values().stream().mapToInt(Integer::intValue).sum() - intersectionCount;
    return totalWords == 0 ? 0 : (double) intersectionCount / totalWords;
  }
} 
