package com.cressy.MWHotelReservations.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LoginDto {
    private String email;
    private String password;
}
