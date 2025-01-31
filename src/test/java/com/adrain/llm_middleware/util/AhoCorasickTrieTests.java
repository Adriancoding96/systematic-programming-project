package com.adrain.llm_middleware.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * AhoCorasickTrieTests is a test class that verifies the correct functionality
 * of the {@link AhoCorasickTrie} data structure.
 */
@SpringBootTest
@ActiveProfiles("test")
public class AhoCorasickTrieTests {

  private AhoCorasickTrie trie;

  @BeforeEach
  public void setUp() {
    trie = new AhoCorasickTrie();
  }

  /**
   * Tests that the trie is initialized correctly.
   * <p>
   *     Verifies that:
   * </p>
   * <ul>
   *   <li>The trie root is not {@code null}.</li>
   *   <li>The trie root is an instance of {@link AhoCorasickNode}.</li>
   * </ul>
   */
  @Test
  public void testInitialization() {
    assertNotNull(trie.getRoot());
    assertTrue(trie.getRoot() instanceof AhoCorasickNode);
  }


   /**
   * Tests inserting a single word into the trie.
   * <p>
   *     Verifies that:
   * </p>
   * <ul>
   *   <li>All characters of the inserted word can be navigated from the root node.</li>
   *   <li>No intermediate node is null.</li>
   * </ul>
   */ 
  @Test
  public void testInsertSingleWord() {
    trie.insert("java");
    AhoCorasickNode root = (AhoCorasickNode) trie.getRoot();
    
    assertNotNull(root.getChildren()['j']);
    assertNotNull((AhoCorasickNode) root
        .getChildren()['j']
        .getChildren()['a']
        .getChildren()['v']
        .getChildren()['a']);
  }

  /**
   * Tests inserting multiple words into the trie at once.
   * <p>
   *     Verifies that:
   * </p>
   * <ul>
   *   <li>Each inserted word's characters are correctly linked in the trie.</li>
   *   <li>No intermediate node is null for each inserted word.</li>
   * </ul>
   */
  @Test
  public void testInsertAllWords() {
    trie.insertAll(Arrays.asList("java", "rust", "cpp"));
    AhoCorasickNode root = (AhoCorasickNode) trie.getRoot();
    
    assertNotNull(root.getChildren()['j']);
    assertNotNull((AhoCorasickNode) root
        .getChildren()['j']
        .getChildren()['a']
        .getChildren()['v']
        .getChildren()['a']);
    
    assertNotNull(root.getChildren()['r']);
    assertNotNull((AhoCorasickNode) root
        .getChildren()['r']
        .getChildren()['u']
        .getChildren()['s']
        .getChildren()['t']);
    
    assertNotNull(root.getChildren()['c']);
    assertNotNull((AhoCorasickNode) root
        .getChildren()['c']
        .getChildren()['p']
        .getChildren()['p']);
  }

  /**
   * Tests building failure links for the trie.
   * <p>
   *     Verifies that:
   * </p>
   * <ul>
   *   <li>Nodes have a valid failure link, particularly the {@code 'j'} node links back to root.</li>
   *   <li>Failure links are properly computed for subsequent characters.</li>
   * </ul>
   */
  @Test
  public void testBuildFailureLinks() {
    trie.insertAll(Arrays.asList("java", "javascript", "typescript", "ecmascript"));
    trie.buildFailureLinks();

    AhoCorasickNode root = (AhoCorasickNode) trie.getRoot();
    AhoCorasickNode jNode = (AhoCorasickNode) root.getChildren()['j'];
    assertNotNull(jNode.getFailureLink());
    assertEquals(root, jNode.getFailureLink());
  }

  /**
   * Tests searching text that contains known patterns.
   * <p>
   *     Verifies that:
   * </p>
   * <ul>
   *   <li>All inserted words are found in the given text.</li>
   *   <li>The returned list of matches includes all the expected words.</li>
   * </ul>
   */
  @Test
  public void testSearchText() {
    trie.insertAll(Arrays.asList("java", "javascript", "typescript", "ecmascript"));
    trie.buildFailureLinks();

    List<String> result = trie.searchText("my name is adrian i like programming in java, typescript is okey, not a big fan of javascript or ecmascript as some call it");
    assertTrue(result.contains("java"));
    assertTrue(result.contains("javascript"));
    assertTrue(result.contains("typescript"));
    assertTrue(result.contains("ecmascript"));
  }

  /**
   * Tests searching text that does not contain any of the inserted patterns.
   * <p>
   *     Verifies that:
   * </p>
   * <ul>
   *   <li>No matches are found for words not present in the text.</li>
   *   <li>The returned list of matches is empty.</li>
   * </ul>
   */
  @Test
  public void testSearchTextWithoutMatches() {
    trie.insertAll(Arrays.asList("java", "javascript", "typescript", "ecmascript"));
    trie.buildFailureLinks();

    List<String> result = trie.searchText("rust");
    assertTrue(result.isEmpty());
  }


  /**
   * Tests searching an empty text.
   * <p>
   *     Verifies that:
   * </p>
   * <ul>
   *   <li>No matches are found when the text is empty.</li>
   *   <li>The returned list of matches is empty.</li>
   * </ul>
   */
  @Test
  public void testSearchEmptyText() {
    trie.insertAll(Arrays.asList("java", "javascript", "typescript", "ecmascript"));
    trie.buildFailureLinks();

    List<String> result = trie.searchText("");
    assertTrue(result.isEmpty());
  }
  
}
