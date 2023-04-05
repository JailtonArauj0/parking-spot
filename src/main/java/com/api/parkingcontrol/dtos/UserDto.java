package com.api.parkingcontrol.dtos;

import com.api.parkingcontrol.models.RolesModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
public class UserDto {

    private UUID userId;

    @NotBlank
    private String username;

    private List<RolesModel> roles;
}
