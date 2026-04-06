package com.seatreservation.repository;

import com.seatreservation.entity.SeatInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatReservationAndInventoryRepository extends JpaRepository<SeatInventory,Long> {
    List<SeatInventory> findByShowIdAndSeatNumberIn(String showId, List<String> seats);

    List<SeatInventory> findByCurrentBookingId(String bookingId);
}
