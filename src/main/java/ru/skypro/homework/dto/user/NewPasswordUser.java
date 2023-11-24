package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NewPasswordUser {

    @Schema(description = "текущий пароль", minLength = 8, maxLength = 16)
    private String currentPassword;

    @Schema(description = "новый пароль", minLength = 8, maxLength = 16)
    private String newPassword;
}
