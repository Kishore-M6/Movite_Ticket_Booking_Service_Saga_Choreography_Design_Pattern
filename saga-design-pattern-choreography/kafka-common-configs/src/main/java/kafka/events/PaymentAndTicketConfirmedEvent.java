package kafka.events;

public record PaymentAndTicketConfirmedEvent(String bookingId,Boolean paymentCompleted,Double amount) {
}
