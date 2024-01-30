package com.testtask_effectivemobile.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthUserDto{
    private String email;
    private String password;
}
