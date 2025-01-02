package com.adrain.llm_middleware;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import com.adrain.llm_middleware.util.AhoCorasickNode;
import com.adrain.llm_middleware.util.AhoCorasickTrie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AhoCorasickTrieTests {

  private AhoCorasickTrie trie;

  @BeforeEach
  public void setUp() {
    trie = new AhoCorasickTrie();
  }

  @Test
  public void testInitialization() {
    assertNotNull(trie.getRoot());
    assertTrue(trie.getRoot() instanceof AhoCorasickNode);
  }

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

  @Test
  public void testBuildFailureLinks() {
    trie.insertAll(Arrays.asList("java", "javascript", "typescript", "ecmascript"));
    trie.buildFailureLinks();

    AhoCorasickNode root = (AhoCorasickNode) trie.getRoot();
    AhoCorasickNode jNode = (AhoCorasickNode) root.getChildren()['j'];
    assertNotNull(jNode.getFailureLink());
    assertEquals(root, jNode.getFailureLink());
  }

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

  @Test
  public void testSearchTextWithoutMatches() {
    trie.insertAll(Arrays.asList("java", "javascript", "typescript", "ecmascript"));
    trie.buildFailureLinks();

    List<String> result = trie.searchText("rust");
    assertTrue(result.isEmpty());
  }

  @Test
  public void testSearchEmptyText() {
    trie.insertAll(Arrays.asList("java", "javascript", "typescript", "ecmascript"));
    trie.buildFailureLinks();

    List<String> result = trie.searchText("");
    assertTrue(result.isEmpty());
  }
  
}
