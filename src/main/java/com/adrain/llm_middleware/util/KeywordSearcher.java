package com.adrain.llm_middleware.util;

import java.util.ArrayList;
import java.util.List;

public class KeywordSearcher {
 
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
    "e3"
  );

  private final int K = 26; // possible letters in the alphabet

  private class Vertex {
    int[] next = new int[K];
    boolean output = false;
    int p = -1;
    char pch;
    int link = -1;
    int[] go = new int[K];

    Vertex(int p, char pch) {
      this.p = p;
      this.pch = pch;

      for(int i = 0; i < K; i++) {
        next[i] = -1;
        go[i] = -1;
      }
    }

    Vertex() {
      this(-1, '$');
    }
  }

  private List<Vertex> vertexes = new ArrayList<>();

  public KeywordSearcher() {
    vertexes.add(new Vertex());
  }


  private void addString(String s) {
    int v = 0;
    char[] chars = s.toCharArray();
    for(char ch : chars) {
      int c = ch - 'a';
      if(vertexes.get(v).next[c] == -1) {
        vertexes.get(v).next[c] = vertexes.size();
        vertexes.add(new Vertex(v, ch));
      }
      v = vertexes.get(v).next[c];
    }
    vertexes.get(v).output = true;

  }

  private int getLink(int v) {
    if(vertexes.get(v).link == -1) {
      if(v == 0 || vertexes.get(v).p == 0) {
        vertexes.get(v).link = 0;
      } else {
        vertexes.get(v).link = go(vertexes.get(v).p, vertexes.get(v).pch);
      }
    }
    return vertexes.get(v).link;
  }

  private int go(int v, char ch) {
    int c = ch - 'a';
    if(vertexes.get(v).go[c] == -1) {
      if(vertexes.get(v).next[c] != -1) {
        vertexes.get(v).go[c] = vertexes.get(v).next[c];
      } else {
        vertexes.get(v).go[c] = v == 0 ? 0 : go(getLink(v), ch);
      }
    }
    return vertexes.get(v).go[c];
  }

  
  
}
