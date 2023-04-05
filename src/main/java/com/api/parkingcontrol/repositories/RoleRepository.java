package com.api.parkingcontrol.repositories;

import com.api.parkingcontrol.models.RoleName;
import com.api.parkingcontrol.models.RolesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RolesModel, UUID> {

    RolesModel findRolesModelByRoleName(RoleName roleName);


}
