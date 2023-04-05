package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.RoleName;
import com.api.parkingcontrol.models.RolesModel;
import com.api.parkingcontrol.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleService {

    private RoleRepository roleRepository;

    public RolesModel findByRole(String roleName) {
        if (roleName.equalsIgnoreCase("ROLE_ADMIN")) {
            return roleRepository.findRolesModelByRoleName(RoleName.ROLE_ADMIN);
        } else if (roleName.equalsIgnoreCase("ROLE_USER")) {
            return roleRepository.findRolesModelByRoleName(RoleName.ROLE_USER);
        }
        return null;
    }

    public RolesModel save(RolesModel rolesModel) {
        return roleRepository.save(rolesModel);
    }

    public Optional<RolesModel> findById(UUID id) {
        return roleRepository.findById(id);
    }

    public List<RolesModel> findAll() {
        return roleRepository.findAll();
    }

    public void delete(UUID id) {
        roleRepository.deleteById(id);
    }


}
