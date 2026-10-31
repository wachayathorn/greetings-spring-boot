package com.wachayathorn.greetings_spring_boot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wachayathorn.greetings_spring_boot.config.ErrorProperties;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorProperties errorProperties;

    public GlobalExceptionHandler(ErrorProperties errorProperties) {
        this.errorProperties = errorProperties;
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ProblemDetail handleDuplicateUsername(DuplicateUsernameException exception) {
        String detail = errorProperties.includeDetails()
                ? exception.getMessage()
                : "Username already taken";
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, detail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                errorProperties.includeDetails() ? "Validation failed" : "Request validation failed");
        if (errorProperties.includeDetails()) {
            problem.setProperty(
                    "errors",
                    exception.getBindingResult().getFieldErrors().stream()
                            .map(error -> error.getField() + ": " + error.getDefaultMessage())
                            .toList());
        }
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception exception) {
        String detail = errorProperties.includeDetails() && exception.getMessage() != null
                ? exception.getMessage()
                : "Unexpected error";
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, detail);
    }
}
