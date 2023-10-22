package ru.skypro.homework.dto.user;

import lombok.Data;

@Data
public class UserDto {

    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String role;
    private String image;
}
