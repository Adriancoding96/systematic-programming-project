package com.adrain.llm_middleware;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.adrain.llm_middleware.enums.ResponseRating;
import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.repository.ResponseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ResponseRepositoryTest {
  
  @Mock
  private ResponseRepository responseRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    Response response = new Response();
    response.setResponseBody("You use css LOL");
    response.setMetaData(new ArrayList<>(Arrays.asList("css", "html", "js")));
    response.setRating(ResponseRating.VERY_USEFUL);
    response.setPrompt(null);

    when(responseRepository.findById(1L)).thenReturn(Optional.of(response));
    when(responseRepository.findAll()).thenReturn(Arrays.asList(response));
 
    when(responseRepository.save(any(Response.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));
  }

  @Test
  public void testFindById() {
    Optional<Response> optionalResponse = responseRepository.findById(1L);
    assertThat(optionalResponse).isNotEmpty();
    assertThat(optionalResponse.get().getResponseBody()).isEqualTo("You use css LOL");
    assertThat(!optionalResponse.get().getMetaData().isEmpty());
    assertThat(optionalResponse.get().getRating()).isEqualTo(ResponseRating.VERY_USEFUL);
  }

  @Test
  public void findAll() {
    List<Response> responses = responseRepository.findAll();
    assertThat(responses).isNotEmpty();
    assertThat(responses).hasSize(1);
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
    assertThat(savedResponse.getRating()).isEqualTo(ResponseRating.VERY_USEFUL);
    assertThat(savedResponse.getResponseBody()).isEqualTo("You use css LOL");
    assertThat(!savedResponse.getMetaData().isEmpty());
  }
}
