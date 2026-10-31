package com.wachayathorn.greetings_spring_boot.booking.service;

import com.wachayathorn.greetings_spring_boot.session.security.AuthenticatedUser;
import com.wachayathorn.greetings_spring_boot.booking.dto.response.BookingResponse;
import com.wachayathorn.greetings_spring_boot.booking.dto.request.CreateBookingRequest;

public interface BookingCommandService {

    BookingResponse create(CreateBookingRequest request, AuthenticatedUser owner);

    BookingResponse cancel(long bookingId, AuthenticatedUser actor);
}
