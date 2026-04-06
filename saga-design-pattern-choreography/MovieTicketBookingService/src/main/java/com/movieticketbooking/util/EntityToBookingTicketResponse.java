package com.movieticketbooking.util;

import com.movieticketbooking.entity.BookingTicketInfo;
import kafka.response.BookingTicketResponse;

public class EntityToBookingTicketResponse {
    public static BookingTicketResponse mapEntityToBookingTicketResponse(BookingTicketInfo bookingTicketInfo) {
        return new BookingTicketResponse(bookingTicketInfo.getBookingId(), bookingTicketInfo.getStatus());
    }
}
