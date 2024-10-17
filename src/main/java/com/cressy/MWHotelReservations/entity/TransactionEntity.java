package com.cressy.MWHotelReservations.entity;

import com.cressy.MWHotelReservations.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class TransactionEntity extends BaseEntity{
    private String referenceId;
    private BigDecimal price;
    private TransactionStatus status;


    @OneToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE
            ,CascadeType.PERSIST,CascadeType.REFRESH})
//    @JoinColumn(name = "order_id")
    private Reservations reservations;
}
