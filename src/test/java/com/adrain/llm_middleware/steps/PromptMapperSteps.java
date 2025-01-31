package com.adrain.llm_middleware.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.adrain.llm_middleware.mapper.PromptMapper;
import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.record.prompt.PromptRecord;
import com.adrain.llm_middleware.record.prompt.PromptRequest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PromptMapperSteps {

    private PromptRequest promptRequest;
    private Prompt prompt;
    private PromptRecord promptRecord;
    private PromptMapper promptMapper = new PromptMapper();

    @Given("a PromptRequest with prompt {string}")
    public void a_prompt_request_with_prompt(String promptText) {
        promptRequest = new PromptRequest("Hello, World!", "gpt4");
    }

    @When("the PromptRequest is mapped to a Prompt")
    public void the_prompt_request_is_mapped_to_a_prompt() {
        prompt = promptMapper.toPromptFromRequest(promptRequest);
    }

    @Then("the Prompt should have the prompt {string}")
    public void the_prompt_should_have_the_prompt(String expectedPrompt) {
        assertEquals(expectedPrompt, prompt.getPrompt());
    }

    @Given("a Prompt with prompt {string} and UUID {string}")
    public void a_prompt_with_prompt_and_uuid(String promptText, String uuid) {
        prompt = new Prompt();
        prompt.setPrompt(promptText);
        prompt.setUuid(uuid);
    }

    @When("the Prompt is mapped to a PromptRecord")
    public void the_prompt_is_mapped_to_a_prompt_record() {
        promptRecord = promptMapper.toRecordFromPrompt(prompt);
    }

    @Then("the PromptRecord should have the prompt {string} and UUID {string}")
    public void the_prompt_record_should_have_the_prompt_and_uuid(String expectedPrompt, String expectedUuid) {
        assertEquals(expectedPrompt, promptRecord.prompt());
        assertEquals(expectedUuid, promptRecord.uuid());
    }
}
