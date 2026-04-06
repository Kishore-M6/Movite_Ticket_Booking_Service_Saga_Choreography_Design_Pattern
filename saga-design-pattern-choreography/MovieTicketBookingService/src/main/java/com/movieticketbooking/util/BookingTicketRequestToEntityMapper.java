package com.movieticketbooking.util;

import com.movieticketbooking.entity.BookingTicketInfo;
import kafka.enums.Status;
import kafka.request.BookingTicketRequest;

import java.time.Instant;
import java.util.UUID;

public class BookingTicketRequestToEntityMapper {
    public static BookingTicketInfo mapBookingTicketRequestToBookingTicketInfo(BookingTicketRequest bookingTicketRequest){

       String bookingId= UUID.randomUUID().toString().split("-")[0];
        BookingTicketInfo bookingTicketInfo = new BookingTicketInfo();

        bookingTicketInfo.setId(System.currentTimeMillis());
        bookingTicketInfo.setUserId(bookingTicketRequest.userId());
        bookingTicketInfo.setBookingId(bookingId);
        bookingTicketInfo.setSeatIds(bookingTicketRequest.seatIds());
        bookingTicketInfo.setStatus(Status.CONFIRMED);
        bookingTicketInfo.setBookedAt(Instant.now());
        bookingTicketInfo.setShowId(bookingTicketRequest.showId());
        bookingTicketInfo.setAmount(bookingTicketRequest.amount());

        return bookingTicketInfo;
    }
}
