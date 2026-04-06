package com.movieticketbooking.repository;

import com.movieticketbooking.entity.BookingTicketInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieTicketBookingRepository extends JpaRepository<BookingTicketInfo,Long> {

BookingTicketInfo findBookingTicketInfoByBookingId(String bookingId);
}
