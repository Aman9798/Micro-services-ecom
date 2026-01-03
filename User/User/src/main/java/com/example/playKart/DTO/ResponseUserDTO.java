package com.example.playKart.DTO;

import com.example.playKart.Entity.Address;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseUserDTO {
    private String name;

    private String email;

    private String phoneNumber;

    private boolean isAdmin;
}
