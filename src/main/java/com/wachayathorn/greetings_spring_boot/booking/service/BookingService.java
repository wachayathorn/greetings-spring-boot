package com.wachayathorn.greetings_spring_boot.booking.service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wachayathorn.greetings_spring_boot.booking.dto.request.CreateBookingRequest;
import com.wachayathorn.greetings_spring_boot.booking.dto.response.BookingResponse;
import com.wachayathorn.greetings_spring_boot.booking.entity.BookingEntity;
import com.wachayathorn.greetings_spring_boot.booking.exception.BookingAlreadyCancelledException;
import com.wachayathorn.greetings_spring_boot.booking.exception.BookingCancelForbiddenException;
import com.wachayathorn.greetings_spring_boot.booking.exception.BookingNotFoundException;
import com.wachayathorn.greetings_spring_boot.booking.exception.BookingOverlapException;
import com.wachayathorn.greetings_spring_boot.booking.exception.InvalidBookingTimeException;
import com.wachayathorn.greetings_spring_boot.booking.repository.BookingRepository;
import com.wachayathorn.greetings_spring_boot.room.entity.RoomEntity;
import com.wachayathorn.greetings_spring_boot.room.exception.RoomNotFoundException;
import com.wachayathorn.greetings_spring_boot.room.repository.RoomRepository;
import com.wachayathorn.greetings_spring_boot.session.security.AuthenticatedUser;
import com.wachayathorn.greetings_spring_boot.user.entity.UserEntity;
import com.wachayathorn.greetings_spring_boot.user.repository.UserRepository;

@Service
public class BookingService {

    private static final Duration MIN_DURATION = Duration.ofMinutes(15);
    private static final Duration MAX_DURATION = Duration.ofHours(8);

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final Clock clock;

    public BookingService(
            BookingRepository bookingRepository,
            RoomRepository roomRepository,
            UserRepository userRepository,
            Clock clock) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    @Transactional
    public BookingResponse create(CreateBookingRequest request, AuthenticatedUser owner) {
        validateTime(request.start(), request.end());

        RoomEntity room = roomRepository
                .findByNameIgnoreCase(request.room())
                .orElseThrow(RoomNotFoundException::new);

        UserEntity user = userRepository
                .findById(owner.userId())
                .orElseThrow(() -> new IllegalStateException("Authenticated user missing"));

        if (bookingRepository.existsActiveRoomOverlap(room.getId(), request.start(), request.end())) {
            throw new BookingOverlapException();
        }
        if (bookingRepository.existsActiveUserOverlap(user.getId(), request.start(), request.end())) {
            throw new BookingOverlapException();
        }

        BookingEntity booking = new BookingEntity(room, user, request.start(), request.end());
        bookingRepository.save(booking);
        return BookingResponse.active(
                booking.getId(),
                room.getName(),
                booking.getStartAt(),
                booking.getEndAt(),
                user.getUsername());
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> listBookings(AuthenticatedUser viewer) {
        return bookingRepository.findVisibleForUser(viewer.userId()).stream()
                .map(BookingService::toResponse)
                .toList();
    }

    @Transactional
    public BookingResponse cancel(long bookingId, AuthenticatedUser actor) {
        BookingEntity booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

        if (!booking.getUser().getId().equals(actor.userId())) {
            throw new BookingCancelForbiddenException();
        }
        if (booking.isCancelled()) {
            throw new BookingAlreadyCancelledException();
        }

        LocalDateTime currentMinute = LocalDateTime.now(clock).truncatedTo(ChronoUnit.MINUTES);
        if (!booking.getEndAt().isAfter(currentMinute)) {
            throw new BookingCancelForbiddenException();
        }

        booking.cancel(currentMinute);
        return BookingResponse.cancelled(
                booking.getId(),
                booking.getRoom().getName(),
                booking.getStartAt(),
                booking.getEndAt(),
                booking.getUser().getUsername());
    }

    private void validateTime(LocalDateTime start, LocalDateTime end) {
        if (!isWholeMinute(start) || !isWholeMinute(end)) {
            throw new InvalidBookingTimeException("Times must be whole minutes");
        }
        if (!end.isAfter(start)) {
            throw new InvalidBookingTimeException("End must be after start");
        }

        Duration length = Duration.between(start, end);
        if (length.compareTo(MIN_DURATION) < 0) {
            throw new InvalidBookingTimeException("Booking must be at least 15 minutes");
        }
        if (length.compareTo(MAX_DURATION) > 0) {
            throw new InvalidBookingTimeException("Booking must be at most 8 hours");
        }

        LocalDateTime currentMinute = LocalDateTime.now(clock).truncatedTo(ChronoUnit.MINUTES);
        if (start.isBefore(currentMinute)) {
            throw new InvalidBookingTimeException("Start must be the current minute or later");
        }
    }

    private static boolean isWholeMinute(LocalDateTime value) {
        return value.getSecond() == 0 && value.getNano() == 0;
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
