package com.delivery.DeliveryTask.exception;

public class TripNotFoundException extends RuntimeException{
    public TripNotFoundException(String message){
        super(message);
    }

}
