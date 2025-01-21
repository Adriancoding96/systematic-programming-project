package com.adrain.llm_middleware.repository;

import java.util.List;
import java.util.Optional;

import com.adrain.llm_middleware.model.Response;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResponseRepository extends JpaRepository<Response, Long> {

  @Query("SELECT r FROM Response r WHERE r.user.email = :email")
  List<Response> findAllByUserEmail(@Param("email") String email);

  @Query("SELECT r FROM Response r WHERE r.prompt.id = :promptId")
  Optional<Response> findByPromptId(@Param("promptId") Long promptId);

  @Query("SELECT r FROM Response r WHERE LOWER(r.responseBody) LIKE LOWER(CONCAT('%', :responseBody, '%')) AND r.user.email = :email")
  List<Response> searchByResponseBodyAndUserEmail(@Param("responseBody") String responseBody, @Param("email") String email);

}
