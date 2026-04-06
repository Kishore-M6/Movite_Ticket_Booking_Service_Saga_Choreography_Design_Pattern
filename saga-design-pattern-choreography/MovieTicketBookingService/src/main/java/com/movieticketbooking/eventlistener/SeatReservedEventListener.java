package com.movieticketbooking.eventlistener;

import com.movieticketbooking.service.MovieTicketBookingService;
import kafka.commons.KafkaTopicConfig;
import kafka.events.SeatReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SeatReservedEventListener {

    private final  MovieTicketBookingService movieTicketBookingService;


    @KafkaListener(topics = KafkaTopicConfig.SEAT_RESERVATION_EVENTS_TOPIC, groupId = KafkaTopicConfig.MOVIE_TICKET_BOOKING_EVENTS_GROUP)
    public void handleSeatReserveEvents(SeatReservedEvent seatReservedEvent) {
        String method="MovieTicketBookingService::SeatReservedEventListener:: handleSeatReserveEvents()";
        log.info(method,"Consuming seatReserved event........");

        if(seatReservedEvent.reserved()){
            log.info("{} Booking process completed for bookingId: {}",method, seatReservedEvent.bookingId());
        }else{
            //rollback
            log.info("{} Seat reservation failed for bookingId: {}", method,seatReservedEvent.bookingId());
            movieTicketBookingService.handleBookingOnSeatReservationFailure(seatReservedEvent.bookingId());
        }

    }}
