package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dtos.UserDto;
import com.api.parkingcontrol.models.RolesModel;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.services.RoleService;
import com.api.parkingcontrol.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/parking-spot-user")
public class UserController {
    private UserService userService;
    private RoleService roleService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<UserModel> user = userService.findById(id);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user.get(), userDto);
        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
    }

    @GetMapping
    public ResponseEntity<List<?>> findAll() {
        List<UserModel> listUsers = userService.findAll();
        List<UserDto> usersDto = new ArrayList<>();


        for (UserModel user : listUsers) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            usersDto.add(userDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(usersDto);
    }

    @PostMapping
    public ResponseEntity<UserModel> saveUser(@RequestBody UserModel userModel) {
        List<RolesModel> roles = userModel.getRoles();
        String roleName = "";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (RolesModel role : roles) {
            roleName = String.valueOf(role.getRoleName());
        }
        RolesModel role = roleService.findByRole(roleName);
        List<RolesModel> addRole = new ArrayList<>();
        addRole.add(role);
        userModel.setRoles(addRole);
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userModel));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody UserModel userModel) {
        Optional<UserModel> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userService.save(userModel));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        Optional<UserModel> user = userService.findById(id);
        if (user.isPresent()) {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
    }

}
