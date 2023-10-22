package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordUser;
import ru.skypro.homework.dto.user.UserChangeDtoIn;
import ru.skypro.homework.dto.user.UserChangeDtoOut;
import ru.skypro.homework.dto.user.UserDto;

public interface UserService {

    boolean userLogged();

    boolean changePassword(NewPasswordUser newPassword);

    UserDto getUserDto();

    UserChangeDtoOut putUser(UserChangeDtoIn userChange);

    void putUserImage(MultipartFile image);
}
