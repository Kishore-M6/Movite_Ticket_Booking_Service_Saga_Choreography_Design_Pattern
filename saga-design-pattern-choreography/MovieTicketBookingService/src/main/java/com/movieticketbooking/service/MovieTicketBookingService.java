package com.movieticketbooking.service;

import com.movieticketbooking.entity.BookingTicketInfo;
import com.movieticketbooking.eventpublisher.BookingStartedEventPublisher;
import com.movieticketbooking.repository.MovieTicketBookingRepository;
import com.movieticketbooking.util.BookingTicketRequestToEntityMapper;
import com.movieticketbooking.util.EntityToBookingTicketResponse;
import kafka.enums.Status;
import kafka.events.BookingStartedEvent;
import kafka.request.BookingTicketRequest;
import kafka.response.BookingTicketResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieTicketBookingService {
    private final MovieTicketBookingRepository movieTicketBookingRepository;
    private final BookingStartedEventPublisher bookingStartedEventPublisher;

    public BookingTicketResponse bookingTicket(BookingTicketRequest bookingTicketRequest) {

        String method="MovieTicketBookingService:: bookingTicket(BookingTicketRequest bookingTicketRequest)";
        log.info("{} booking the seats for user {} for the show {}",method, bookingTicketRequest.userId(), bookingTicketRequest.showId());
//        map request to entity
        BookingTicketInfo bookingTicketInfo = BookingTicketRequestToEntityMapper.mapBookingTicketRequestToBookingTicketInfo(bookingTicketRequest);
//       save and map to response
        com.movieticketbooking.entity.BookingTicketInfo saved = movieTicketBookingRepository.save(bookingTicketInfo);


//        publish booking started event

        kafka.events.BookingStartedEvent bookingStartedEvent = buildBookingStartedEvent(saved);
        bookingStartedEventPublisher.publishBookingStartedEvent(bookingStartedEvent);
        kafka.response.BookingTicketResponse bookingTicketResponse = EntityToBookingTicketResponse.mapEntityToBookingTicketResponse(saved);
        return bookingTicketResponse;
    }

    private BookingStartedEvent buildBookingStartedEvent(BookingTicketInfo saved) {
        return new BookingStartedEvent(saved.getBookingId(), saved.getUserId(), saved.getShowId(), saved.getSeatIds(), saved.getAmount());
    }


    public void handleBookingOnSeatReservationFailure(String bookingId) {
        String method="MovieTicketBookingService:: handleBookingOnSeatReservationFailure()";
        log.info("{} Handling booking failure for bookingId {}",method, bookingId);
        BookingTicketInfo bookingTicketInfo=movieTicketBookingRepository.findBookingTicketInfoByBookingId(bookingId);
        if(bookingTicketInfo!=null){
            bookingTicketInfo.setStatus(Status.CANCELLED);
            movieTicketBookingRepository.save(bookingTicketInfo);
            log.info("{} Booking marked as FAILED for bookingId {}",method, bookingId);
        }else{
            log.warn("{} No booking found with bookingId {}",method, bookingId);
        }

    }

}
