package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordUser;
import ru.skypro.homework.dto.user.UserChangeDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public boolean userLogged() {
        return false;
    }

    @Override
    public boolean changePassword(NewPasswordUser newPassword) {
        return false;
    }

    @Override
    public UserDto getUserDto() {
        return null;
    }

    @Override
    public UserChangeDto putUser(UserChangeDto userChange) {
        return null;
    }

    @Override
    public void putUserImage(MultipartFile image) {

    }
}
