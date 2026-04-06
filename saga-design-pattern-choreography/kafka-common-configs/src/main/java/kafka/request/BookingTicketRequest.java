package kafka.request;


import java.util.List;

public record BookingTicketRequest(String showId,
                                   List<String> seatIds, String userId,
                                   Double amount) {
}
