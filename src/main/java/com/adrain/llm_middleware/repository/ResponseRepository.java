package com.adrain.llm_middleware.repository;

import java.util.List;
import java.util.Optional;

import com.adrain.llm_middleware.model.Response;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResponseRepository extends JpaRepository<Response, Long> {

  @Query("SELECT r FROM Response r WHERE r.user.email = :email")
  List<Response> findAllByUserEmail(String email);

  @Query("SELECT r FROM Response r WHERE r.prompt.id = :promptId")
  Optional<Response> findByPromptId(Long promptId);

}
