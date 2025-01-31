package com.adrain.llm_middleware.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an API error response.
 * This class contains the details of an error response, including the http status code
 * and a error message.
 *
 * <p>The class uses Lombok annotations to automatically generate boilerplate code
 * such as getters, setters, and constructors.</p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiErrorResponse {
  private int statusCode;
  private String message;
}
