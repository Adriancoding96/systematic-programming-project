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
  @JoinColumn(name = "prompt_id")
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
