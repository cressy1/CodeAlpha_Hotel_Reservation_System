package com.cressy.MWHotelReservations.controller;

import com.cressy.MWHotelReservations.dto.HotelResponse;
import com.cressy.MWHotelReservations.dto.LoginDto;
import com.cressy.MWHotelReservations.dto.UserRequest;
import com.cressy.MWHotelReservations.entity.Users;
import com.cressy.MWHotelReservations.exceptions.InvalidTokenException;
import com.cressy.MWHotelReservations.exceptions.TokenExpiredException;
import com.cressy.MWHotelReservations.repositories.UserRepository;
import com.cressy.MWHotelReservations.services.AuthServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthServices authServices;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public HotelResponse register(@RequestBody UserRequest userRequest) {
      return authServices.registerUser(userRequest);
    }

    @PostMapping("/login")
    public HotelResponse register(@RequestBody LoginDto loginDto) {
        return authServices.loginUser(loginDto);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        Users users = userRepository.findByVerificationToken(token)
                .orElseThrow(()-> new InvalidTokenException("Invalid verification token "));

        if (users.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Verification token expired. Please register again.");
        }

        // Mark the user as verified
        users.setIsVerified(true);
        users.setVerificationToken(null);  // Clear the token once verified
        users.setTokenExpiryDate(null);  // Clear the token expiry date
        userRepository.save(users);

        return ResponseEntity.ok("Your account has been successfully verified.");
    }
    }

