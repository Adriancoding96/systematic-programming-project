package com.adrain.llm_middleware.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.adrain.llm_middleware.enums.ResponseRating;
import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.model.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * ResponseRepositoryTest is a test class to verify the correct functionality
 * of {@link ResponseRepository} queries.
 */
@DataJpaTest
@ActiveProfiles("test")
public class ResponseRepositoryTest {

    @Autowired
    private ResponseRepository responseRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Tests that a {@link Response} entity can be found by its ID
     * via {@link ResponseRepository#findById(Long)}.
     * <p>
     *     Verifies that:
     * </p>
     * <ul>
     *   <li>The retrieved {@code Response} is not empty.</li>
     *   <li>The {@code ResponseBody}, metadata, and rating match the saved entity.</li>
     * </ul>
     */
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

    /**
     * Tests that all {@link Response} entities can be fetched via {@link ResponseRepository#findAll()}.
     * <p>
     *     Verifies that:
     * </p>
     * <ul>
     *   <li>The returned list contains the expected number of {@code Response} entities.</li>
     * </ul>
     */
    @Test
    public void findAll() {
      Response response1 = new Response();
      response1.setResponseBody("Response 1");
      response1.setMetaData(List.of("java", "rust"));
      response1.setRating(ResponseRating.VERY_USEFUL);

      Response response2 = new Response();
      response2.setResponseBody("Response 2");
      response2.setMetaData(List.of("zig"));
      response2.setRating(ResponseRating.SLIGHTLY_USEFUL);

      responseRepository.save(response1);
      responseRepository.save(response2);

      List<Response> responses = responseRepository.findAll();
      assertThat(responses).hasSize(2);
    }

    /**
     * Tests that all {@link Response} entities associated with a user email
     * can be fetched via {@link ResponseRepository#findAllByUserEmail(String)}.
     * <p>
     *     Verifies that:
     * </p>
     * <ul>
     *   <li>The returned list contains the expected {@code Response} entities for the given user.</li>
     *   <li>The list size matches the number of saved responses for that user.</li>
     * </ul>
     */
    @Test
    public void findAllByUserEmail() {
      User user = new User();
      user.setName("Adrian");
      user.setEmail("adrian@example.com");
      user.setPassword("verysecurepassword123");
    
      userRepository.save(user);
    
      Response response1 = new Response();
      response1.setResponseBody("Response 1");
      response1.setMetaData(List.of("java", "rust"));
      response1.setRating(ResponseRating.VERY_USEFUL);
      response1.setUser(user);

      Response response2 = new Response();
      response2.setResponseBody("Response 2");
      response2.setMetaData(List.of("zig"));
      response2.setRating(ResponseRating.SLIGHTLY_USEFUL);
      response2.setUser(user);
   
      responseRepository.saveAll(List.of(response1, response2));

      List<Response> responses = responseRepository.findAllByUserEmail("adrian@example.com");
      assertThat(responses).hasSize(2);

    }


    /**
     * Tests that a {@link Response} entity can be saved via {@link ResponseRepository#save(Object)}.
     * <p>
     *     Verifies that:
     * </p>
     * <ul>
     *   <li>The saved {@code Response} has a non-null ID.</li>
     *   <li>The {@code ResponseBody}, metadata, and rating match the entity before saving.</li>
     * </ul>
     */
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
