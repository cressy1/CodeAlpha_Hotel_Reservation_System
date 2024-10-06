package com.cressy.MWHotelReservations.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Users extends BaseEntity{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean isVerified;

    private String verificationToken;
    private LocalDateTime tokenExpiryDate;


    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,
            CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "user-Roles",
            joinColumns = @JoinColumn(name = "user-id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",
                    referencedColumnName = "id"))
    private Collection<Roles> roles = new HashSet<>();

//    public Users(String email, String password, Collection<GrantedAuthority> grantedAuthorities) {
//    }
}
