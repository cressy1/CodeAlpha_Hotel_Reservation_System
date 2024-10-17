package com.cressy.MWHotelReservations.dto;

import com.cressy.MWHotelReservations.entity.Reservations;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private Long transactionId;
    private String referenceId;
    private Reservations reservations;
}
