package com.adrain.llm_middleware;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.adrain.llm_middleware.enums.ResponseRating;
import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.repository.ResponseRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ResponseRepositoryTest {

    @Autowired
    private ResponseRepository responseRepository;

    @Test
    public void testFindById() {
        Response response = new Response();
        response.setResponseBody("You use css LOL");
        response.setMetaData(new ArrayList<>(Arrays.asList("css", "html", "js")));
        response.setRating(ResponseRating.VERY_USEFUL);
        response.setPrompt(null);

        Response saved = responseRepository.save(response);

        Optional<Response> optionalResponse = responseRepository.findById(saved.getId());

        assertThat(optionalResponse).isNotEmpty();
        assertThat(optionalResponse.get().getResponseBody()).isEqualTo("You use css LOL");
        assertThat(optionalResponse.get().getMetaData()).contains("css", "html", "js");
        assertThat(optionalResponse.get().getRating()).isEqualTo(ResponseRating.VERY_USEFUL);
    }

    @Test
    public void findAll() {
        Response r1 = new Response();
        r1.setResponseBody("Response 1");
        r1.setMetaData(List.of("tag1", "tag2"));
        r1.setRating(ResponseRating.VERY_USEFUL);

        Response r2 = new Response();
        r2.setResponseBody("Response 2");
        r2.setMetaData(List.of("tag3"));
        r2.setRating(ResponseRating.SLIGHTLY_USEFUL);

        responseRepository.save(r1);
        responseRepository.save(r2);

        List<Response> responses = responseRepository.findAll();
        assertThat(responses).hasSize(2);
    }

    @Test
    public void testSave() {
        Response response = new Response();
        response.setResponseBody("You use css LOL");
        response.setMetaData(new ArrayList<>(Arrays.asList("css", "html", "js")));
        response.setRating(ResponseRating.VERY_USEFUL);
        response.setPrompt(null);

        Response savedResponse = responseRepository.save(response);

        assertThat(savedResponse).isNotNull();
        assertThat(savedResponse.getId()).isNotNull();
        assertThat(savedResponse.getResponseBody()).isEqualTo("You use css LOL");
        assertThat(savedResponse.getMetaData()).containsExactlyInAnyOrder("css", "html", "js");
        assertThat(savedResponse.getRating()).isEqualTo(ResponseRating.VERY_USEFUL);
    }
}
