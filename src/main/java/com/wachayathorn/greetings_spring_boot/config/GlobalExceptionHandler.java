package com.wachayathorn.greetings_spring_boot.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wachayathorn.greetings_spring_boot.booking.exception.BookingAlreadyCancelledException;
import com.wachayathorn.greetings_spring_boot.booking.exception.BookingCancelForbiddenException;
import com.wachayathorn.greetings_spring_boot.booking.exception.BookingNotFoundException;
import com.wachayathorn.greetings_spring_boot.booking.exception.BookingOverlapException;
import com.wachayathorn.greetings_spring_boot.booking.exception.InvalidBookingTimeException;
import com.wachayathorn.greetings_spring_boot.room.exception.RoomNotFoundException;
import com.wachayathorn.greetings_spring_boot.session.exception.InvalidCredentialsException;
import com.wachayathorn.greetings_spring_boot.session.exception.InvalidSessionTokenException;
import com.wachayathorn.greetings_spring_boot.session.exception.SessionAlreadyLoggedOutException;
import com.wachayathorn.greetings_spring_boot.user.exception.DuplicateUsernameException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorProperties errorProperties;

    public GlobalExceptionHandler(ErrorProperties errorProperties) {
        this.errorProperties = errorProperties;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(InvalidCredentialsException exception) {
        String detail = errorProperties.includeDetails()
                ? exception.getMessage()
                : "Invalid username or password";
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, detail);
    }

    @ExceptionHandler({
        InvalidSessionTokenException.class,
        SessionAlreadyLoggedOutException.class
    })
    public ProblemDetail handleInvalidSession(RuntimeException exception) {
        String detail = errorProperties.includeDetails()
                ? exception.getMessage()
                : "Authentication required";
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, detail);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ProblemDetail handleRoomNotFound(RoomNotFoundException exception) {
        String detail = errorProperties.includeDetails() ? exception.getMessage() : "Room not found";
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, detail);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ProblemDetail handleBookingNotFound(BookingNotFoundException exception) {
        String detail = errorProperties.includeDetails() ? exception.getMessage() : "Booking not found";
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, detail);
    }

    @ExceptionHandler(BookingOverlapException.class)
    public ProblemDetail handleBookingOverlap(BookingOverlapException exception) {
        String detail = errorProperties.includeDetails() ? exception.getMessage() : "Booking conflict";
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, detail);
    }

    @ExceptionHandler(BookingAlreadyCancelledException.class)
    public ProblemDetail handleBookingAlreadyCancelled(BookingAlreadyCancelledException exception) {
        String detail = errorProperties.includeDetails() ? exception.getMessage() : "Booking already cancelled";
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, detail);
    }

    @ExceptionHandler(BookingCancelForbiddenException.class)
    public ProblemDetail handleBookingCancelForbidden(BookingCancelForbiddenException exception) {
        String detail = errorProperties.includeDetails() ? exception.getMessage() : "Cannot cancel this booking";
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, detail);
    }

    @ExceptionHandler(InvalidBookingTimeException.class)
    public ProblemDetail handleInvalidBookingTime(InvalidBookingTimeException exception) {
        String detail = errorProperties.includeDetails() ? exception.getMessage() : "Invalid booking time";
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
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
