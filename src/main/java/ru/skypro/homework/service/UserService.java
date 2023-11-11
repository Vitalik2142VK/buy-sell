package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordUser;
import ru.skypro.homework.dto.user.UserChangeDto;
import ru.skypro.homework.dto.user.UserDto;

import java.io.IOException;

public interface UserService {

    boolean userLogged();

    boolean changePassword(NewPasswordUser newPassword);

    UserDto getUserDto(UserDetails userDetails);

    UserChangeDto putUser(UserChangeDto userChange);

    void putUserImage(MultipartFile image) throws IOException;
}
