package com.movieticketbooking.entity;


import jakarta.persistence.*;
import kafka.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "Booking_Ticket_Info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingTicketInfo {
    @Id
    private Long id;
    private String bookingId;
    private String userId;
    private String showId;
    @ElementCollection
    private List<String> seatIds;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Instant bookedAt;
    private Double amount;
}
