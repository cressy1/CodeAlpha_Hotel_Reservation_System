package com.cressy.MWHotelReservations.services;

import com.cressy.MWHotelReservations.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
    void sendEmailWithReservationDetails(EmailDetails emailDetails);
}
