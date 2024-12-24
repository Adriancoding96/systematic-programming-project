package com.adrain.llm_middleware.repository;

import com.adrain.llm_middleware.model.Response;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response, Long> {}
