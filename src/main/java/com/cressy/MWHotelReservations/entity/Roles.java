package com.cressy.MWHotelReservations.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Roles extends BaseEntity{
    private String name;
    //use a collection simply because at a future time the Hashset can easily be changed to any type of collections eg a List.
    @ManyToMany(mappedBy = "roles")
    private Collection<Users> users = new HashSet<>();

    public Roles(String name) {
        this.name = name;
    }

    //Assign role to a user
    public void assignRoleToUser(Users user) {
        user.getRoles().add(this);
        this.getUsers().add(user);
    }

    //Remove a user from a role
    public void removeUserFromRole(Users user) {
        user.getRoles().remove(this);
        this.getUsers().remove(user);
    }
    //Remove All Users from role
    public void removeAllUsersFromRole() {
        if (this.getUsers() != null) {
            List<Users> roleUsers = this.getUsers()
                    .stream().toList();
            roleUsers.forEach(this :: removeUserFromRole);

        }
    }
    public String getName() {
        return name != null? name : "";
    }
}
