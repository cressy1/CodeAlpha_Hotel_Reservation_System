package com.cressy.MWHotelReservations.services.ServiceImplementations;

import com.cressy.MWHotelReservations.entity.Roles;
import com.cressy.MWHotelReservations.entity.Users;
import com.cressy.MWHotelReservations.exceptions.RoleAlreadyExistsException;
import com.cressy.MWHotelReservations.exceptions.UserAlreadyExistsException;
import com.cressy.MWHotelReservations.repositories.RoleRepository;
import com.cressy.MWHotelReservations.repositories.UserRepository;
import com.cressy.MWHotelReservations.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    @Override
    public List<Roles> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Roles createRole(Roles theRole) {
        String roleName = "ROLE_"+theRole.getName().toUpperCase();
        Roles role = new Roles(roleName);
        if (roleRepository.existsByName(roleName)) {
            throw new RoleAlreadyExistsException(theRole.getName()+" role already exists");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        this.removeAllUsersFromRole(id);
        roleRepository.deleteById(id);
    }

    @Override
    public Roles findByName(String name) {
        return roleRepository.findByName(name).get();
    }

    @Override
    public Users removeUserFromRole(Long userId, Long roleId) {
        Optional<Users> user = userRepository.findById(userId);
        Optional<Roles> role = roleRepository.findById(roleId);
        if (role.isPresent() && role.get().getUsers().contains(user.get())) {
            role.get().removeUserFromRole(user.get());
            roleRepository.save(role.get());
            return user.get();
        }
        throw new UsernameNotFoundException("User not found");    }

    @Override
    public Users assignRoleToUser(Long userId, Long roleId) {
        Optional<Users> user = userRepository.findById(userId);
        Optional<Roles> role = roleRepository.findById(roleId);
        if (user.isPresent() && user.get().getRoles().contains(role.get())) {
            throw new UserAlreadyExistsException(
                    user.get().getFirstName()+" is already assigned to the"
                            + role.get().getName()+ " role");
        }
        if (role.isPresent()) {
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());

        }
        return user.get();    }

    @Override
    public Roles removeAllUsersFromRole(Long roleId) {
        Optional<Roles> role = roleRepository.findById(roleId);
        role.get().removeAllUsersFromRole();
        return roleRepository.save(role.get());    }
}
