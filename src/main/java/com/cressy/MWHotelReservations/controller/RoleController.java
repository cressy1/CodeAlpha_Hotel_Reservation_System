package com.cressy.MWHotelReservations.controller;

import com.cressy.MWHotelReservations.entity.Roles;
import com.cressy.MWHotelReservations.entity.Users;
import com.cressy.MWHotelReservations.exceptions.RoleAlreadyExistsException;
import com.cressy.MWHotelReservations.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth/")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/all-roles")
    public ResponseEntity<List<Roles>> getAllRoles() {
        return new ResponseEntity<>(roleService.getRoles(), HttpStatus.FOUND);
    }

    @PostMapping("/create-new-role")
    public ResponseEntity<String> createRole(@RequestBody Roles theRole) {
        try {
            roleService.createRole(theRole);
            return ResponseEntity.ok("New role created successfully");
        } catch (RoleAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        }
    }

    @DeleteMapping("/delete/{roleId}")
    public void deleteRole(@PathVariable("roleId") Long roleId) {
        roleService.deleteRole(roleId);

    }

    @PostMapping("/remove-all-users-from-role/{roleId}")
    public Roles removeAllUsersFromRole(@PathVariable("roleId") Long roleId) {
        return roleService.removeAllUsersFromRole(roleId);
    }

    @PostMapping("/remove-single-user-from-role")
    public Users removeSingleUserFromRole(@RequestParam("userId") Long userId,
                                          @RequestParam("roleId") Long roleId) {
        return roleService.removeUserFromRole(userId, roleId);
    }

    @PostMapping("/assign-user-to-role")
    public Users assignRoleToUser(@RequestParam("userId") Long userId,
                                 @RequestParam("roleId") Long roleId) {
        return roleService.assignRoleToUser(userId, roleId);
    }

}
