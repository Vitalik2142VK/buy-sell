package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.UserAuth;
import ru.skypro.homework.dto.user.NewPasswordUser;
import ru.skypro.homework.dto.user.UserChangeDto;
import ru.skypro.homework.dto.user.UserDto;

import java.io.IOException;

public interface UserService {

    boolean changePassword(NewPasswordUser newPassword, UserAuth userDetails);
    UserDto getUserDto(UserAuth userDetails);
    UserChangeDto putUser(UserChangeDto userChange, UserAuth userDetails);
    void putUserImage(MultipartFile image, UserAuth userDetails) throws IOException;
    byte[] getImage(String nameImage) throws IOException;
}
