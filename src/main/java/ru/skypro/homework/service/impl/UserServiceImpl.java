package ru.skypro.homework.service.impl;

import ru.skypro.homework.dto.user.NewPasswordUser;
import ru.skypro.homework.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public boolean changePassword(NewPasswordUser newPassword) {
        return false;
    }
}
