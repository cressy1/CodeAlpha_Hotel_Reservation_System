package com.cressy.MWHotelReservations.repositories;

import com.cressy.MWHotelReservations.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
