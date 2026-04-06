package com.movieticketbooking.eventpublisher;

import kafka.events.BookingStartedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static kafka.commons.KafkaTopicConfig.MOVIE_TICKET_BOOKING_EVENTS_TOPIC;

@Component
@Slf4j
public class BookingStartedEventPublisher {
    @Autowired
    private KafkaTemplate<String, BookingStartedEvent> kafkaTemplate;

    public void publishBookingStartedEvent(BookingStartedEvent bookingStartedEvent) {
        String method = "BookingStartedEventProducer::publishBookingStartedEvent(BookingStartedEvent bookingStartedEvent)";
        try {

            log.info("{} publishing BookingStartedEvent with id: {}", method, bookingStartedEvent.bookingId());

            kafkaTemplate.send(MOVIE_TICKET_BOOKING_EVENTS_TOPIC, bookingStartedEvent.bookingId(), bookingStartedEvent);

        } catch (Exception e) {
            log.error("{} Error while publishing BookingStartedEvent for id : {}", method, bookingStartedEvent.bookingId(), e);
        }
    }
}
