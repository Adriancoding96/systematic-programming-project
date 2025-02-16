package com.adrain.llm_middleware.controller;


import java.util.List;

import com.adrain.llm_middleware.util.AhoCorasickTrie;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

  private final AhoCorasickTrie ahoCorasickTrie;
 
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
  

  public TestController() {
    this.ahoCorasickTrie = new AhoCorasickTrie();
    this.ahoCorasickTrie.insertAll(KEYWORDS);
    this.ahoCorasickTrie.buildFailureLinks();
  }

  @PostMapping("/search")
  public ResponseEntity<List<String>> searchPatterns(@RequestBody String text) {
    List<String> matches = ahoCorasickTrie.searchText(text);
    return ResponseEntity.ok(matches);
  }
}
