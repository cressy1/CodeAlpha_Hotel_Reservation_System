package com.cressy.MWHotelReservations.services;

import com.cressy.MWHotelReservations.entity.Roles;
import com.cressy.MWHotelReservations.entity.Users;

import java.util.List;

public interface RoleService {
    List<Roles> getRoles();
    Roles createRole(Roles theRole);
    void deleteRole(Long id);
    Roles findByName(String name);
    Users removeUserFromRole(Long userId, Long roleId);
    Users assignRoleToUser(Long userId, Long roleId);
    Roles removeAllUsersFromRole(Long roleId);
}
