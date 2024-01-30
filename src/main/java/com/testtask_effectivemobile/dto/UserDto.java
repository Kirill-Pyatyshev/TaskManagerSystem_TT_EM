package com.testtask_effectivemobile.dto;

import lombok.*;

import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto{
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
}
