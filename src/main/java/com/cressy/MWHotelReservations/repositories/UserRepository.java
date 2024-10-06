package com.cressy.MWHotelReservations.repositories;

import com.cressy.MWHotelReservations.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);  // Find user by email
    boolean existsByEmail(String email);
    Optional<Users> findByVerificationToken(String token);
    void deleteByEmail(String email);
}
