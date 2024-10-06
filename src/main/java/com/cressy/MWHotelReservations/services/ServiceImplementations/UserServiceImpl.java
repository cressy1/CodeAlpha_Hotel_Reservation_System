package com.cressy.MWHotelReservations.services.ServiceImplementations;

import com.cressy.MWHotelReservations.dto.HotelResponse;
import com.cressy.MWHotelReservations.entity.Users;
import com.cressy.MWHotelReservations.exceptions.UserNotFoundException;
import com.cressy.MWHotelReservations.repositories.UserRepository;
import com.cressy.MWHotelReservations.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public List<Users> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String email) {
        Users theUser = getAUser(email);
        if (theUser != null) {
            userRepository.deleteByEmail(email);
        }
        userRepository.deleteByEmail(email);
    }

    @Override
    public Users getAUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->
                new UserNotFoundException("User not found"));
    }
}
