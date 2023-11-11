package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.UserAuthDetailsService;
import ru.skypro.homework.dto.user.NewPasswordUser;
import ru.skypro.homework.dto.user.UserChangeDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundUserException;
import ru.skypro.homework.mapping.UserMapper;
import ru.skypro.homework.model.WorkWithImage;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${user.image}")
    private String imagePath;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public boolean userLogged() {
        return false;
    }

    /**
    * Change the user's password.
    */
    @Override
    public boolean changePassword(NewPasswordUser newPassword) {
        return false;
    }

    /**
     * Provides data about the current user.
     */
    @Override
    public UserDto getUserDto(UserDetails userDetails) {
        User user = userRepository.findFirstByEmail(userDetails.getUsername()).orElseThrow(NotFoundUserException::new);
        return userMapper.mapToUserDto(user);
    }

    /**
     * Change data about the current user.
     */
    @Override
    public UserChangeDto putUser(UserChangeDto userChange) {
        int id = 1; //TODO Заменить поиск Id от пользователя
        User user = userMapper.mapToUserForUserChangeDto(
                userChange,
                userRepository.findById(id).orElseThrow(NotFoundUserException::new));
        userRepository.save(user);
        return userChange;
    }

    /**
     * Update the user's image.
     */
    @Override
    public void putUserImage(MultipartFile image) throws IOException {
        int id = 1; //TODO Заменить поиск Id от пользователя
        User user = userRepository.findById(id).orElseThrow(NotFoundUserException::new);
        user.setImage(WorkWithImage.saveAndGetStringImage(imagePath, user.getImage(), image));
        userRepository.save(user);
    }
}
