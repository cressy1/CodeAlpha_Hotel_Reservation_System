package com.cressy.MWHotelReservations.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Rooms extends BaseEntity{
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked = false;
    @Lob
    private Blob photo;

    @OneToMany(mappedBy = "rooms", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservations> reservations;

    public Rooms() {
        this.reservations = new ArrayList<>();
    }
    public void addReservation(Reservations reservation) {
        if (reservations == null) {
            reservations = new ArrayList<>();
        }
        reservations.add(reservation);
        reservation.setRooms(this);
        isBooked = true;
        String bookingCode = RandomStringUtils.random(10);
        reservation.setBookingConfirmationCode(bookingCode);
    }
}
