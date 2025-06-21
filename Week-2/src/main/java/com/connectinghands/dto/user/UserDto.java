package com.connectinghands.dto.user;

import lombok.Data;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roles;
} 