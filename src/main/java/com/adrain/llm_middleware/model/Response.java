package com.adrain.llm_middleware.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import com.adrain.llm_middleware.enums.ResponseRating;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a response entity in the API.
 * This class is mapped to a database table and contains details about a response,
 * including its id, associated {@link Prompt}, response body, metadata, {@link ResponseRating},
 * and {@link User}.
 *
 * <p>The class uses Lombok annotations to automatically generate 
 * getters, setters, constructors, and {@code toString}.</p>
 *
 * @see Prompt
 * @see ResponseRating
 * @see User
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Response {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @OneToOne
  private Prompt prompt;

  @Column(columnDefinition = "TEXT")
  private String responseBody;

  private List<String> metaData;

  private ResponseRating rating;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private User user;

}
