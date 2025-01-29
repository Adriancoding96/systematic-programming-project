package com.adrain.llm_middleware.model;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a prompt entity in the API.
 * This class is mapped to a database table and contains details about a prompt,
 * including its id, uuid, prompt text, {@link User}
 * and {@link Response}.
 *
 * <p>The class uses Lombok annotations to automatically generate
 * getters, setters, constructors, and {@code toString}.</p>
 *
 * <p>The uuid is automatically generated before persisting or updating the entity
 * if it is not already set.</p>
 *
 * @see User
 * @see Response
 * @see PrePersist
 * @see PreUpdate
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Prompt {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, updatable = false)
  private String uuid;

  private String prompt;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private User user;
 
  @OneToOne(mappedBy = "prompt")
  private Response response;

  @PrePersist
  @PreUpdate
  public void generateUuid() {
    if(uuid != null) return;
    uuid = UUID.randomUUID().toString();
  }
  
}
