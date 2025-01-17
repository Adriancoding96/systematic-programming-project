package com.adrain.llm_middleware.util;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


/**
 * Trie is a prefix tree data structure that stores and retrieves strings.
 * <p>
 *     This implementation uses a {@link TrieNode} as the root node, providing
 *     operations to insert multiple words, insert a single word, and search
 *     for a given word in the trie.
 * </p>
 */
@Getter
@Setter
public class Trie {

    private TrieNode root;

    /**
     * Constructs a Trie by initializing the {@link #root} node.
     */
    public Trie() {
        root = new TrieNode();
    }

    /**
     * Inserts a list of words into this trie.
     *
     * @param words The list of words to insert into the trie.
     */
    public void insertAll(List<String> words) {
        for (String word : words) {
            insert(word);
        }
    }

    /**
     * Inserts a single word into this trie.
     *
     * @param word The word to be inserted.
     */
    public void insert(String word) {
        TrieNode current = root;

        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            int index = ch;

            if (current.getChildren()[index] == null) {
                current.getChildren()[index] = new TrieNode();
            }
            current = current.getChildren()[index];
        }
        current.setEndOfWord(true);
    }

    /**
     * Searches for a word in this trie.
     * <p>
     *     Traverses the trie based on the characters in the word to determine
     *     if the path to the final node exists and is marked as an end of a word.
     * </p>
     *
     * @param word The word to search for.
     * @return {@code true} if the word exists in the trie, otherwise {@code false}.
     */
    public boolean search(String word) {
        TrieNode current = root;

        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            int index = ch;

            if (current.getChildren()[index] == null) {
                return false;
            }
            current = current.getChildren()[index];
        }
        return current != null && current.isEndOfWord();
    }
}
