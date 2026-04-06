package com.movieticketbooking.controller;

import com.movieticketbooking.service.MovieTicketBookingService;
import kafka.request.BookingTicketRequest;
import kafka.response.BookingTicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/movie_ticket_booking")
@RequiredArgsConstructor
public class MovieTicketBookingController {

    private final MovieTicketBookingService movieTicketBookingService;

    // Endpoint to handle ticket / seat booking requests
    @PostMapping(value = "/booking_ticket")
    public ResponseEntity<?> handleMovieTicketBooking(@RequestBody BookingTicketRequest bookingTicketRequest) {

        BookingTicketResponse bookingTicketResponse = movieTicketBookingService.bookingTicket(bookingTicketRequest);
        return ResponseEntity.ok(bookingTicketResponse);
    }
}
