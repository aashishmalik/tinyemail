package com.example.tinyemail.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
import java.util.List;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "An invalid or missing field was found.")
public class InvalidFieldException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  public InvalidFieldException(List<FieldError> fieldErrorList) {
    this(fieldErrorList, null);
  }

  public InvalidFieldException(List<FieldError> fieldErrorList, Throwable cause) {
    super();
  }

}
