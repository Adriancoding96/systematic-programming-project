Feature: Prompt Mapper

  Scenario: Map a PromptRequest to a Prompt
    Given a PromptRequest with prompt "Hello, World!"
    When the PromptRequest is mapped to a Prompt
    Then the Prompt should have the prompt "Hello, World!"

  Scenario: Map a Prompt to a PromptRecord
    Given a Prompt with prompt "Hello, World!" and UUID "12345"
    When the Prompt is mapped to a PromptRecord
    Then the PromptRecord should have the prompt "Hello, World!" and UUID "12345"
