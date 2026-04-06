package com.seatreservation.service;

import com.seatreservation.entity.SeatInventory;
import com.seatreservation.enums.SeatStatus;
import com.seatreservation.eventpublisher.SeatReservedEventPublisher;
import com.seatreservation.repository.SeatReservationAndInventoryRepository;
import kafka.events.BookingStartedEvent;
import kafka.events.PaymentAndTicketConfirmedEvent;
import kafka.events.SeatReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatReservationAndInventoryService {
    private final SeatReservationAndInventoryRepository seatReservationAndInventoryRepository;
    private final SeatReservedEventPublisher seatReservedEventPublisher;

    public void handleBookingTicketBasedOnSeatInventory(BookingStartedEvent bookingStartedEvent) {
        String method = "SeatReservationAndInventoryService:: handleBookingTicketBasedOnSeatInventory(BookingStartedEvent bookingStartedEvent)";
        log.info( "{}  Processing bookingCreated for bookingId {}",method, bookingStartedEvent.bookingId());
        // Fetch seat inventories for the given show and seat numbers

        java.util.List<SeatInventory> seats = seatReservationAndInventoryRepository.findByShowIdAndSeatNumberIn(bookingStartedEvent.showId(), bookingStartedEvent.seats());

        //        checking all seats are available or not
        boolean availability=seats.stream().allMatch(seat-> seat.getStatus() == SeatStatus.AVAILABLE);

        if (availability) {
            // Update seat status to LOCKED and set current booking ID
            seats.forEach(s -> {
                        s.setStatus(SeatStatus.LOCKED);
                        s.setCurrentBookingId(bookingStartedEvent.bookingId());
                    }
            );
            seatReservationAndInventoryRepository.saveAll(seats);
            // Publish seat reserved event
               seatReservedEventPublisher.publishSeatReservedEvent(new SeatReservedEvent(bookingStartedEvent.bookingId(),bookingStartedEvent.amount(),true));
            log.info("{} Seats locked successfully for bookingId {}",method, bookingStartedEvent.bookingId());
        }else {
            log.warn("{}  Seat locking failed for bookingId {}. Some seats are not available.",method, bookingStartedEvent.bookingId());
            // publish seat reserved event with failure
            seatReservedEventPublisher.publishSeatReservedEvent(new SeatReservedEvent(bookingStartedEvent.bookingId(),bookingStartedEvent.amount(),false));
        }
    }
    public void releaseSeatsOnPaymentFailure(String bookingId) {
        String method="seatReservationAndInventoryService:: releaseSeatsOnPaymentFailure() ";
        log.info("{} SeatInventoryService:: Releasing seats for bookingId {}",method , bookingId);


        List<SeatInventory> bookingSeats = seatReservationAndInventoryRepository.findByCurrentBookingId(bookingId);

        bookingSeats.forEach(s -> {
            s.setStatus(SeatStatus.AVAILABLE);
            s.setCurrentBookingId(null);
        });

        seatReservationAndInventoryRepository.saveAll(bookingSeats);
        log.info("{} Seats released successfully for bookingId {}",method,bookingId);

        //send failed event to downstream (booking-service)
        seatReservedEventPublisher
                .publishSeatReservedEvent(new SeatReservedEvent(bookingId, 0.0, false));
    }


    public void updateSeatStatusByHandlingPaymentAndTicketConfirmedEvent(String bookingId) {

        String method="SeatReservationAndInventoryService :: updateSeatStatusByHandlingPaymentAndTicketConfirmedEvent(String bookingId)";
        log.info("{}  updating seat status by listening PaymentAndTicketConfirmedEvent for bookingId {}",method, bookingId);
        List<SeatInventory> bookingSeats = seatReservationAndInventoryRepository.findByCurrentBookingId(bookingId);

        bookingSeats.forEach(s -> {
            s.setStatus(SeatStatus.RESERVED);
        });
        seatReservationAndInventoryRepository.saveAll(bookingSeats);
    }
}
