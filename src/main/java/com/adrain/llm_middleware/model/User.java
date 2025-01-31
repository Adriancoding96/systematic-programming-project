package com.adrain.llm_middleware.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a user entity in the API.
 * This class is mapped to a database table and contains details about a user,
 * including their ID, name, email, password, and associated {@link Prompt}s
 * and {@link Response}s.
 *
 * <p>The class uses Lombok annotations to automatically create
 * getters, setters, constructors, and {@code toString}.</p>
 *
 * <p>The table name is explicitly set to "users" to avoid conflicts with reserved keywords
 * in the Postgres database.</p>
 * 
 * @see Prompt
 * @see Response
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "users")
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  
  private String email;
  
  private String password;

  @OneToMany(mappedBy = "user")
  private List<Prompt> prompts;

  @OneToMany(mappedBy = "user")
  private List<Response> responses; 
}
