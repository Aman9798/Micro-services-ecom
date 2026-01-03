package com.example.user.DTO;

import lombok.*;

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
