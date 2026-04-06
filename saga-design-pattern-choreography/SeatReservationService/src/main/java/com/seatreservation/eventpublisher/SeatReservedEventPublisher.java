package com.seatreservation.eventpublisher;

import kafka.events.SeatReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static kafka.commons.KafkaTopicConfig.SEAT_RESERVATION_EVENTS_TOPIC;

@Component
@Slf4j
public class SeatReservedEventPublisher {
    @Autowired
    private KafkaTemplate<String,SeatReservedEvent> kafkaTemplate;

    public void publishSeatReservedEvent(SeatReservedEvent seatReservedEvent) {
        String  method = "SeatReservedEventProducer::publishSeatReservedEvent(SeatReservedEvent seatReservedEvent)";
        try {

            log.info("{} publishing SeatReservedEvent with id: {}", method, seatReservedEvent.bookingId());

            kafkaTemplate.send(SEAT_RESERVATION_EVENTS_TOPIC, seatReservedEvent.bookingId(), seatReservedEvent);

        } catch (Exception e) {
            log.error("{} SeatReservedEventProducer:: Error while publishing SeatReserved event for bookingId {}: {}", method,  seatReservedEvent.bookingId(), e);
        }
    }
}
