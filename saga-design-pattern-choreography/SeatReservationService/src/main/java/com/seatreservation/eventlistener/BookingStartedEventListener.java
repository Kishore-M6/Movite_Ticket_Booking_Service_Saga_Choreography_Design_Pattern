package com.seatreservation.eventlistener;

import com.seatreservation.service.SeatReservationAndInventoryService;
import kafka.commons.KafkaTopicConfig;
import kafka.events.BookingStartedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


import static kafka.commons.KafkaTopicConfig.MOVIE_TICKET_BOOKING_EVENTS_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingStartedEventListener {
    private final SeatReservationAndInventoryService seatReservationAndInventoryService;

    @KafkaListener(topics = {MOVIE_TICKET_BOOKING_EVENTS_TOPIC}, groupId = KafkaTopicConfig.SEAT_RESERVATION_EVENTS_GROUP, autoStartup = "true")
    public void handleBookingStartedEvent(BookingStartedEvent bookingStartedEvent) {
        String method = "BookingStartedEventListener::handleBookingStartedEvent";
        // Logic to update seat reservation and inventory based on Booking Started Event
        log.info( "{}  Consuming booking started event for bookingId {}", method,bookingStartedEvent.bookingId());
        seatReservationAndInventoryService.handleBookingTicketBasedOnSeatInventory(bookingStartedEvent);
    }
}
