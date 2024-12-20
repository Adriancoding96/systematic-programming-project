
package com.adrain.llm_middleware.repository;

import com.adrain.llm_middleware.model.Prompt;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
  
}
