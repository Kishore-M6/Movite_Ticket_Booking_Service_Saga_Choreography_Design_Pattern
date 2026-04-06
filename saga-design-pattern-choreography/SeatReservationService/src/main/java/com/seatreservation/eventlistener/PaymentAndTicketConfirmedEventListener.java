package com.seatreservation.eventlistener;

import com.seatreservation.service.SeatReservationAndInventoryService;
import kafka.commons.KafkaTopicConfig;
import kafka.events.BookingStartedEvent;
import kafka.events.PaymentAndTicketConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static kafka.commons.KafkaTopicConfig.MOVIE_TICKET_BOOKING_EVENTS_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentAndTicketConfirmedEventListener {
    private final SeatReservationAndInventoryService seatReservationAndInventoryService;

    @KafkaListener(topics = {KafkaTopicConfig.PAYMENT_AND_TICKET_CONFIRMATION_EVENTS_TOPIC}, groupId = KafkaTopicConfig.SEAT_RESERVATION_EVENTS_GROUP)
    public void handlePaymentAndTicketConfirmedEvent(PaymentAndTicketConfirmedEvent paymentAndTicketConfirmedEvent) {
        String method = "PaymentAndTicketConfirmedEventListener::handlePaymentAndTicketConfirmedEvent() ";

        log.info("{} Consuming Booking payment status event {}",method,  paymentAndTicketConfirmedEvent.bookingId());
        if (paymentAndTicketConfirmedEvent.paymentCompleted()) {
            log.info("{} Payment status succeeded for bookingId: {}",method, paymentAndTicketConfirmedEvent.bookingId());
            seatReservationAndInventoryService.updateSeatStatusByHandlingPaymentAndTicketConfirmedEvent(paymentAndTicketConfirmedEvent.bookingId());
        } else {
            log.info("{} Payment failed for bookingId: {}, releasing seats.",method,  paymentAndTicketConfirmedEvent.bookingId());
            seatReservationAndInventoryService.releaseSeatsOnPaymentFailure(paymentAndTicketConfirmedEvent.bookingId());
        }
    }
}
