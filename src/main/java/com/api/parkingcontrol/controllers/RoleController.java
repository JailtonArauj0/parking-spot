package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.models.RolesModel;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.services.RoleService;
import com.api.parkingcontrol.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/parking-spot-roles")
public class RoleController {

    private RoleService roleService;
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<RolesModel>> findAll() {
        List<RolesModel> roles = roleService.findAll();
        if (!roles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(roleService.findAll());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolesModel> findById(@PathVariable UUID id) {
        Optional<RolesModel> role = roleService.findById(id);
        if (role.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(role.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<RolesModel> save(@RequestBody RolesModel rolesModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.save(rolesModel));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Optional<RolesModel> roles = roleService.findById(id);

        if (roles.isPresent()) {
            List<UserModel> listUsers = userService.findAll();
            for (UserModel user : listUsers) {
                List<RolesModel> userRoles = user.getRoles();
                for (RolesModel role : userRoles){
                    UUID roleId = role.getRoleId();
                    if (roleId == id) {
                        userService.deleteById(user.getUserId());
                    }
                }
            }
            roleService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role não encontrada.");
    }

}
