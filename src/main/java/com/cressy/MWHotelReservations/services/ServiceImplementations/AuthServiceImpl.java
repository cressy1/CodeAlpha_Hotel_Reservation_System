package com.cressy.MWHotelReservations.services.ServiceImplementations;

import com.cressy.MWHotelReservations.dto.EmailDetails;
import com.cressy.MWHotelReservations.dto.HotelResponse;
import com.cressy.MWHotelReservations.dto.LoginDto;
import com.cressy.MWHotelReservations.dto.UserRequest;
import com.cressy.MWHotelReservations.entity.Roles;
import com.cressy.MWHotelReservations.entity.Users;
import com.cressy.MWHotelReservations.exceptions.DuplicateEmailException;
import com.cressy.MWHotelReservations.repositories.RoleRepository;
import com.cressy.MWHotelReservations.repositories.UserRepository;
import com.cressy.MWHotelReservations.security.JWTTokenProvider;
import com.cressy.MWHotelReservations.services.AuthServices;
import com.cressy.MWHotelReservations.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthServices {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final JWTTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;


    @Override
    public HotelResponse registerUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail().toLowerCase())) {
            throw new DuplicateEmailException("This email already exists");
        }
        Users users = new Users();
        users.setFirstName(userRequest.getFirstName());
        users.setLastName(userRequest.getLastName());
        users.setEmail(userRequest.getEmail());
        users.setPassword(passwordEncoder.encode(userRequest.getEmail()));

        // Generate a random token
        String verificationToken = RandomStringUtils.randomAlphanumeric(20);
        users.setVerificationToken(verificationToken);

        // Set the token expiry to 5 minutes from now
        users.setTokenExpiryDate(LocalDateTime.now().plusMinutes(5));


        Roles userRole = roleRepository.findByName("ROLE_USER").get();//on registration, a user has the role of ROLE_USER
        users.setRoles(Collections.singletonList(userRole));
        users.setIsVerified(false);

        Users savedUser = userRepository.save(users);

        // Create verification URL
        String verificationLink = "http://yourdomain.com/api/auth/verify?token=" + verificationToken;


        //send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("MWHotels Registration")
                .messageBody("Please verify your account using the following link: " + verificationLink)
                .build();
        emailService.sendEmailAlert(emailDetails);

        return HotelResponse.builder()
                .responseCode("201")
                .responseMessage("Verification link sent to " + userRequest.getEmail() +
                        ", Check email and verify your account")
                .build();
    }

    @Override
    public HotelResponse loginUser(LoginDto loginDto) {
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
               loginDto.getEmail(), loginDto.getPassword()));

        EmailDetails loginAlert = EmailDetails.builder()
                .subject("you are logged in!!!")
                .recipient(loginDto.getEmail())
                .messageBody("You logged into your account. If you did not initiate this request please contact customer care on 09051548332. \n Thank you")
                .build();
        emailService.sendEmailAlert(loginAlert);


        return HotelResponse.builder()
                .responseCode("201 Success")
                .responseMessage(jwtTokenProvider.generateJwtToken(authentication))
                .build();
    }
}
