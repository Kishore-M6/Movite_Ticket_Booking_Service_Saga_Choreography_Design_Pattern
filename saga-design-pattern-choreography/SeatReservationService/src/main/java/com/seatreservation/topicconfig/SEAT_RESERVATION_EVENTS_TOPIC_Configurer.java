package com.seatreservation.topicconfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static kafka.commons.KafkaTopicConfig.MOVIE_TICKET_BOOKING_EVENTS_TOPIC;
import static kafka.commons.KafkaTopicConfig.SEAT_RESERVATION_EVENTS_TOPIC;


@Configuration
public class SEAT_RESERVATION_EVENTS_TOPIC_Configurer {

    @Bean
    public NewTopic createTopic(){
        return new NewTopic(SEAT_RESERVATION_EVENTS_TOPIC,3,(short)1);
    }
}
