package com.taskmanager.taskmanager.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegisterRequest {
    private String email;
    private String password;
    private String role;
    private String name;
}
