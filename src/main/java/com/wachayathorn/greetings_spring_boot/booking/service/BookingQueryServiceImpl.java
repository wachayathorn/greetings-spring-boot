package com.wachayathorn.greetings_spring_boot.booking.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wachayathorn.greetings_spring_boot.session.security.AuthenticatedUser;
import com.wachayathorn.greetings_spring_boot.booking.entity.BookingEntity;
import com.wachayathorn.greetings_spring_boot.booking.repository.BookingRepository;
import com.wachayathorn.greetings_spring_boot.booking.dto.response.BookingResponse;

@Service
public class BookingQueryServiceImpl implements BookingQueryService {

    private final BookingRepository bookingRepository;

    public BookingQueryServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> listBookings(AuthenticatedUser viewer) {
        return bookingRepository.findVisibleForUser(viewer.userId()).stream()
                .map(BookingQueryServiceImpl::toResponse)
                .toList();
    }

    private static BookingResponse toResponse(BookingEntity booking) {
        if (booking.isCancelled()) {
            return BookingResponse.cancelled(
                    booking.getId(),
                    booking.getRoom().getName(),
                    booking.getStartAt(),
                    booking.getEndAt(),
                    booking.getUser().getUsername());
        }
        return BookingResponse.active(
                booking.getId(),
                booking.getRoom().getName(),
                booking.getStartAt(),
                booking.getEndAt(),
                booking.getUser().getUsername());
    }
}
