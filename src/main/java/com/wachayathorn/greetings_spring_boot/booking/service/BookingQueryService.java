package com.wachayathorn.greetings_spring_boot.booking.service;

import java.util.List;

import com.wachayathorn.greetings_spring_boot.session.security.AuthenticatedUser;
import com.wachayathorn.greetings_spring_boot.booking.dto.response.BookingResponse;

public interface BookingQueryService {

    List<BookingResponse> listBookings(AuthenticatedUser viewer);
}
