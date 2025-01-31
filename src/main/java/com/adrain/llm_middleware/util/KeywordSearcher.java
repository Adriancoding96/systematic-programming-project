package com.adrain.llm_middleware.util;

import java.util.List;

import com.adrain.llm_middleware.api.OpenAiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * KeywordSearcher is a singleton component responsible for identifying known keywords
 * within a given text using the {@link AhoCorasickTrie} algorithm.
 * <p>
 *     This class maintains a predefined list of keywords and uses {@link AhoCorasickTrie}
 *     for efficient keyword searching. The singleton instance can be retrieved
 *     via {@link #getInstance()}.
 * </p>
 *
 * @see AhoCorasickTrie
 */
@Component
public class KeywordSearcher {

  private static KeywordSearcher instance;

  /**
   * Hard coded keywords API is searching for in responses from {@link OpenAiClient}.
   * */
  private final List<String> KEYWORDS = List.of(
      "java",
      "rust",
      "c",
      "cpp",
      "c++",
      "c#",
      "javascript",
      "typescript",
      "python",
      "bash",
      "ruby",
      "php",
      "sql",
      "kotlin",
      "go",
      "swift",
      "r",
      "dart",
      "scala",
      "perl",
      "lua",
      "objective-c",
      "powershell",
      "basic",
      "haskell",
      "elixir",
      "f#",
      "ocaml",
      "vimscript",
      "odin",
      "zig",
      "assembly",
      "markdown",
      "html",
      "css",
      "cobol",
      "matlab",
      "dreamberd",
      "holyc",
      "spring",
      "hibernate",
      "jakarta-ee",
      "actix",
      "rocket",
      "qt",
      "boost",
      "dotnet",
      "asp.net",
      "react",
      "angular",
      "vue",
      "svelte",
      "next.js",
      "nuxt.js",
      "express",
      "nest.js",
      "django",
      "flask",
      "fastapi",
      "pyramid",
      "rails",
      "sinatra",
      "laravel",
      "symfony",
      "codeigniter",
      "cakephp",
      "ktor",
      "gin",
      "echo",
      "fiber",
      "vapor",
      "kitura",
      "perfect",
      "shiny",
      "plumber",
      "flutter",
      "play",
      "akka",
      "mojolicious",
      "catalyst",
      "dancer",
      "luvit",
      "openresty",
      "cocoa",
      "psframework",
      "yesod",
      "snap",
      "phoenix",
      "suave",
      "giraffe",
      "ocaml-lwt",
      "mysql",
      "postgresql",
      "oracle",
      "mssql",
      "mongodb",
      "redis",
      "cassandra",
      "mariadb",
      "sqlite",
      "elasticsearch",
      "postman",
      "trello",
      "slack",
      "jira",
      "confluence",
      "github",
      "gitlab",
      "bitbucket",
      "docker",
      "kubernetes",
      "terraform",
      "jenkins",
      "aws",
      "azure",
      "google cloud",
      "ibm cloud",
      "oracle cloud",
      "digitalocean",
      "s3",
      "ec2",
      "ecs",
      "lambda",
      "fargate",
      "azure devops",
      "azure functions",
      "google compute engine",
      "google app engine",
      "firebase",
      "cloud run",
      "e3");

  private final AhoCorasickTrie ahoCorasickTrie;

  @Autowired
  private KeywordSearcher() {
    this.ahoCorasickTrie = new AhoCorasickTrie();
    this.ahoCorasickTrie.insertAll(KEYWORDS);
  }

  /**
   * Returns the singleton instance of this {@link KeywordSearcher}.
   *
   * @return the singleton instance.
   */
  public static KeywordSearcher getInstance() {
    if(instance == null) {
      instance = new KeywordSearcher();
    }
    return instance;
  }

  /**
   * Extracts a list of known keywords from the given text using the {@link AhoCorasickTrie}.
   *
   * @param text the text in which to search for keywords.
   * @return a list of matching keywords found within the text.
   */
  public List<String> getKeywords(String text) {
    return this.ahoCorasickTrie.searchText(text);
  }
}
