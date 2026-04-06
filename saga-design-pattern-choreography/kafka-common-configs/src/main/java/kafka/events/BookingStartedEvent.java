package kafka.events;

import java.util.List;

public record BookingStartedEvent(String bookingId, String userId, String showId, List<String> seats, Double amount) {
}
