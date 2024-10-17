package com.cressy.MWHotelReservations.exceptions;

public class InvalidReservationRequestException extends RuntimeException{
    public InvalidReservationRequestException(String message) {
        super(message);
    }
}
