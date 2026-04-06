package com.paymentandticketconfirmation.exception;

public class PaymentFailureException extends RuntimeException{
    public PaymentFailureException(String message){
        super(message);
    }

    public PaymentFailureException() {
    }
}
