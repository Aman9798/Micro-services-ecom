package com.example.user.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRequestDTO {

    @NotNull
    private String userEmail;
}
