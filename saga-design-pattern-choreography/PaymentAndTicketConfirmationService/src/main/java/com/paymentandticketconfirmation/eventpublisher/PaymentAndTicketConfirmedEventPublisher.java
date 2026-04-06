package com.paymentandticketconfirmation.eventpublisher;

import kafka.events.PaymentAndTicketConfirmedEvent;
import kafka.events.SeatReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static kafka.commons.KafkaTopicConfig.PAYMENT_AND_TICKET_CONFIRMATION_EVENTS_TOPIC;


@Component
@Slf4j
public class PaymentAndTicketConfirmedEventPublisher {
    @Autowired
    private KafkaTemplate<String, PaymentAndTicketConfirmedEvent> kafkaTemplate;

    String method = "PaymentAndTicketConfirmedEventPublisher::publishPaymentAndTicketConfirmedEvent(SeatReservedEvent seatReservedEvent)";

    public void publishPaymentSuccessEvent(SeatReservedEvent event){
        log.info("{} Publishing payment success event ...",method);
        PaymentAndTicketConfirmedEvent paymentEvent=new PaymentAndTicketConfirmedEvent(event.bookingId(),true,event.amount());

        kafkaTemplate.send(PAYMENT_AND_TICKET_CONFIRMATION_EVENTS_TOPIC, event.bookingId(),paymentEvent);

    }

    public void publishPaymentFailureEvent(SeatReservedEvent event){
        log.info("{} Publishing payment failure event ...",method);
        PaymentAndTicketConfirmedEvent paymentEvent=new PaymentAndTicketConfirmedEvent(event.bookingId(),false,event.amount());
        kafkaTemplate.send(PAYMENT_AND_TICKET_CONFIRMATION_EVENTS_TOPIC, event.bookingId(),paymentEvent);

    }
}
