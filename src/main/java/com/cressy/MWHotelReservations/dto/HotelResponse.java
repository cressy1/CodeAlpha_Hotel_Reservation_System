package com.cressy.MWHotelReservations.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelResponse {
    private String responseCode;
    private String responseMessage;
}
