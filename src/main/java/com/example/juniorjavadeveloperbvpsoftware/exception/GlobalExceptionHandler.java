package com.example.juniorjavadeveloperbvpsoftware.exception;


import com.example.juniorjavadeveloperbvpsoftware.dto.response.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        logger.error("Argument not valid");
        return ResponseEntity.badRequest().body(new ExceptionResponse("MethodArgumentNotValidException", ex.getBindingResult().toString()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleNullEntityReferenceException(NullEntityReferenceException ex) {
        logger.error("Null entity");
        return ResponseEntity.badRequest().body(new ExceptionResponse("NullEntityReferenceException", ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.error("Entity not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse("EntityNotFoundException", ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex) {
        logger.error(String.format("Failed to authenticate because of: %s", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse("BadCredentialsException", ex.getMessage()));
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<ExceptionResponse> handleCredentialsExpiredException(CredentialsExpiredException ex) {
        logger.error(String.format("Failed to authenticate because of: %s", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse("CredentialsExpiredException", ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse("AuthenticationException", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> globalHandlerException(Exception ex) {
        logger.error(String.format("Failed because of: %s", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse("Exception", ex.getMessage()));
    }
}
