package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordUser;
import ru.skypro.homework.dto.user.UserChangeDto;
import ru.skypro.homework.dto.user.UserDto;

public interface UserService {

    boolean userLogged();

    boolean changePassword(NewPasswordUser newPassword);

    UserDto getUserDto();

    UserChangeDto putUser(UserChangeDto userChange);

    void putUserImage(MultipartFile image);
}
