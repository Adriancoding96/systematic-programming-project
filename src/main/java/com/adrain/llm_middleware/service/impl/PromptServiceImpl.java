package com.adrain.llm_middleware.service.impl;

import java.util.List;

import com.adrain.llm_middleware.api.OpenAiClient;
import com.adrain.llm_middleware.record.Prompt.PromptRequest;
import com.adrain.llm_middleware.record.Prompt.PromptResponse;
import com.adrain.llm_middleware.record.api.OpenAiResponse;
import com.adrain.llm_middleware.repository.PromptRepository;
import com.adrain.llm_middleware.service.PromptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class PromptServiceImpl implements PromptService {

  private final PromptRepository promptRepository;
  private final OpenAiClient openAiClient;

  @Autowired
  public PromptServiceImpl(PromptRepository promptRepository, OpenAiClient openAiClient) {
    this.promptRepository = promptRepository;
    this.openAiClient = openAiClient;
  }

  /*
   * Method extracts response from llm api and returns it to PromptController
   *
   * @param request: record that contains prompt text
   * @return response: returns record that contains response text
   * */
  @Override
  public PromptResponse newPrompt(PromptRequest request) {
    OpenAiResponse fullResponse = getResponse(request.prompt());
    return new PromptResponse(fullResponse.choices().get(0).message().toString());
     
  }

  /*
   * Method calls opanai api client which returns a response if successfull
   *
   * @param prompt: contains prompt from user
   * @return response: contains responses from openai api and meta data
   * */
  private OpenAiResponse getResponse(String prompt) {
    Mono<OpenAiResponse> monoResponse = openAiClient.getCompletion(prompt);
    return monoResponse.block();
  }

  // Create method to extract meta data from api response, words of interest are following, programming languages, packages, frameworks, databases, third party technologies
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

  
}
