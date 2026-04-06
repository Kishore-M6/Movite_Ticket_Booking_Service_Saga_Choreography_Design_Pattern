package com.paymentandticketconfirmation.eventlistener;

import com.paymentandticketconfirmation.service.PaymentAndTicketConfirmationService;
import kafka.commons.KafkaTopicConfig;
import kafka.events.SeatReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static kafka.commons.KafkaTopicConfig.SEAT_RESERVATION_EVENTS_GROUP;
import static kafka.commons.KafkaTopicConfig.SEAT_RESERVATION_EVENTS_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeatReservedEventListener {
    private final PaymentAndTicketConfirmationService paymentAndTicketConfirmationService;

    @KafkaListener(topics = SEAT_RESERVATION_EVENTS_TOPIC , groupId = KafkaTopicConfig.PAYMENT_AND_TICKET_CONFIRMATION_EVENTS_GROUP)
    public void handleSeatReservedEvent(SeatReservedEvent event) {
        String method="PaymentAndTicketConfirmationService:: SeatReservedEventListener::handleSeatReservedEvent()";
        try {
            log.info("{} Consumed SeatReservedEvent for bookingId: {} and event {}",method, event.bookingId(), event);
            if(event.reserved()) {
                paymentAndTicketConfirmationService.processPaymentAndTicketConfirmation(event);
            }else{
                log.info( "{} skipping payment processing as seat reservation failed for bookingId: {}",method, event.bookingId());
            }
        } catch (Exception e) {
            log.error("{} Error processing SeatReservedEvent for bookingId {}: {}",method, event.bookingId(), e);
        }
    }
}
