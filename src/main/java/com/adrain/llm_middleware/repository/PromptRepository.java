package com.adrain.llm_middleware.repository;

import java.util.List;

import com.adrain.llm_middleware.model.Prompt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PromptRepository extends JpaRepository<Prompt, Long> {

  @Query("SELECT p FROM Prompt p WHERE p.user.email = :email")
  List<Prompt> findAllByUserEmail(String email);

}
