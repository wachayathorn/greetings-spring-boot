package com.wachayathorn.greetings_spring_boot.booking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.wachayathorn.greetings_spring_boot.session.security.AuthRequestContext;
import com.wachayathorn.greetings_spring_boot.booking.service.BookingCommandService;
import com.wachayathorn.greetings_spring_boot.booking.service.BookingQueryService;
import com.wachayathorn.greetings_spring_boot.booking.dto.response.BookingResponse;
import com.wachayathorn.greetings_spring_boot.booking.dto.request.CreateBookingRequest;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingCommandService bookingCommandService;
    private final BookingQueryService bookingQueryService;

    public BookingController(
            BookingCommandService bookingCommandService,
            BookingQueryService bookingQueryService) {
        this.bookingCommandService = bookingCommandService;
        this.bookingQueryService = bookingQueryService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody CreateBookingRequest request) {
        BookingResponse response = bookingCommandService.create(request, AuthRequestContext.requireUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> list() {
        List<BookingResponse> bookings = bookingQueryService.listBookings(AuthRequestContext.requireUser());
        return ResponseEntity.status(HttpStatus.OK).body(bookings);
    }

    @PostMapping("/{bookingId}/cancellation")
    public ResponseEntity<BookingResponse> cancel(@PathVariable long bookingId) {
        BookingResponse response = bookingCommandService.cancel(bookingId, AuthRequestContext.requireUser());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
