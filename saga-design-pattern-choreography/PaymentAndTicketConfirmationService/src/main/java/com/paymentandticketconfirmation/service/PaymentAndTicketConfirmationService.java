package com.paymentandticketconfirmation.service;

import com.paymentandticketconfirmation.eventpublisher.PaymentAndTicketConfirmedEventPublisher;
import com.paymentandticketconfirmation.exception.PaymentFailureException;
import kafka.events.PaymentAndTicketConfirmedEvent;
import kafka.events.SeatReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentAndTicketConfirmationService {
    private  final PaymentAndTicketConfirmedEventPublisher paymentAndTicketConfirmedEventPublisher;
    private final KafkaTemplate<String, PaymentAndTicketConfirmedEvent> kafkaTemplate;

    public void processPaymentAndTicketConfirmation(SeatReservedEvent seatReservedEvent) {

        String method="PaymentAndTicketConfirmationService::processPaymentAndTicketConfirmation(SeatReservedEvent seatReservedEvent)";
        try {
            log.info( "{} Processing payment for bookingId: {}",method, seatReservedEvent.bookingId());

            // Simulate payment failure scenario
            if (seatReservedEvent.amount() > 2000) {
                log.info("{}  Payment amount exceeds limit for bookingId: {}",method, seatReservedEvent.bookingId());
//                 failure events and handle events through performing compensating transactions
                paymentAndTicketConfirmedEventPublisher.publishPaymentFailureEvent(seatReservedEvent);

//                throwing exception without performing compensating transactions
//                throw new RuntimeException("Payment amount exceeds limit");
            }else{
                // success event
                paymentAndTicketConfirmedEventPublisher.publishPaymentSuccessEvent(seatReservedEvent);
                log.info("{}  Payment successful for bookingId: {}",method, seatReservedEvent.bookingId());

            }


        }catch (Exception e){
            log.error("{}  Payment failed for bookingId: {}. Reason: {}",method,seatReservedEvent.bookingId(), e.getMessage());
            throw new PaymentFailureException("Payment processing failed for bookingId: " + seatReservedEvent.bookingId());
        }
    }
}
