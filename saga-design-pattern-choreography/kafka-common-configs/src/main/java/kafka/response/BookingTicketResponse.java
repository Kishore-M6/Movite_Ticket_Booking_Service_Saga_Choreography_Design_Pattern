package kafka.response;

import kafka.enums.Status;

public record BookingTicketResponse(String bookingId, Status status) {
}
