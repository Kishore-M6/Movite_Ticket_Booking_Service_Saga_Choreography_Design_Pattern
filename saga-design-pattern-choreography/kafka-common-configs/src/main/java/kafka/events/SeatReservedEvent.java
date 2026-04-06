package kafka.events;

public record SeatReservedEvent(String bookingId,Double amount,boolean reserved) {
}
