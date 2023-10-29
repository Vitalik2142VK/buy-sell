package ru.skypro.homework.dto.user;

import lombok.Data;

@Data
public class NewPasswordUser {

    private String currentPassword;
    private String newPassword;
}
