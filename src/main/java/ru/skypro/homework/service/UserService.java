package ru.skypro.homework.service;

import ru.skypro.homework.dto.user.NewPasswordUser;

public interface UserService {

    boolean changePassword(NewPasswordUser newPassword);
}
