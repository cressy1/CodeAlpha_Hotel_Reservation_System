package com.cressy.MWHotelReservations.services;

import com.cressy.MWHotelReservations.entity.Users;

import java.util.List;

public interface UserService {
    List<Users> getUsers();
    void deleteUser(String email);
    Users getAUser(String email);
}
