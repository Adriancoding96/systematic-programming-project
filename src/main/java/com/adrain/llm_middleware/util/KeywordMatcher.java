package com.adrain.llm_middleware.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.adrain.llm_middleware.model.Prompt;

import org.springframework.stereotype.Component;

@Component
public class KeywordMatcher {

  private final String REGEX = "[a-zA-Z0-9]+";
  private Pattern pattern;

  public KeywordMatcher() {
    pattern = Pattern.compile(REGEX);
  }
  
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
