package com.wachayathorn.greetings_spring_boot.booking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wachayathorn.greetings_spring_boot.session.security.AuthenticatedUser;
import com.wachayathorn.greetings_spring_boot.booking.exception.BookingAlreadyCancelledException;
import com.wachayathorn.greetings_spring_boot.booking.exception.BookingCancelForbiddenException;
import com.wachayathorn.greetings_spring_boot.booking.entity.BookingEntity;
import com.wachayathorn.greetings_spring_boot.booking.exception.BookingNotFoundException;
import com.wachayathorn.greetings_spring_boot.booking.exception.BookingOverlapException;
import com.wachayathorn.greetings_spring_boot.booking.repository.BookingRepository;
import com.wachayathorn.greetings_spring_boot.booking.dto.response.BookingResponse;
import com.wachayathorn.greetings_spring_boot.booking.dto.request.CreateBookingRequest;
import com.wachayathorn.greetings_spring_boot.room.entity.RoomEntity;
import com.wachayathorn.greetings_spring_boot.room.exception.RoomNotFoundException;
import com.wachayathorn.greetings_spring_boot.room.repository.RoomRepository;
import com.wachayathorn.greetings_spring_boot.user.entity.UserEntity;
import com.wachayathorn.greetings_spring_boot.user.repository.UserRepository;

@Service
public class BookingCommandServiceImpl implements BookingCommandService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final BookingTimeValidator bookingTimeValidator;
    private final java.time.Clock clock;

    public BookingCommandServiceImpl(
            BookingRepository bookingRepository,
            RoomRepository roomRepository,
            UserRepository userRepository,
            BookingTimeValidator bookingTimeValidator,
            java.time.Clock clock) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.bookingTimeValidator = bookingTimeValidator;
        this.clock = clock;
    }

    @Override
    @Transactional
    public BookingResponse create(CreateBookingRequest request, AuthenticatedUser owner) {
        bookingTimeValidator.validate(request.start(), request.end());

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

    @Override
    @Transactional
    public BookingResponse cancel(long bookingId, AuthenticatedUser actor) {
        BookingEntity booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

        if (!booking.getUser().getId().equals(actor.userId())) {
            throw new BookingCancelForbiddenException();
        }
        if (booking.isCancelled()) {
            throw new BookingAlreadyCancelledException();
        }

        java.time.LocalDateTime currentMinute =
                java.time.LocalDateTime.now(clock).truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
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
}
