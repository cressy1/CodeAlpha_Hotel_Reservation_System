package com.cressy.MWHotelReservations.services;

import com.cressy.MWHotelReservations.dto.HotelResponse;
import com.cressy.MWHotelReservations.dto.LoginDto;
import com.cressy.MWHotelReservations.dto.UserRequest;
import com.cressy.MWHotelReservations.entity.Users;

public interface AuthServices {
    HotelResponse registerUser(UserRequest userRequest);
    HotelResponse loginUser(LoginDto loginDto);


}
