package ru.skypro.homework.service.impl;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.UserAuth;
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
    private final PasswordEncoder encoder;

    @Value("${user.image}")
    private String imagePath;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    /**
    * Change the user's password.
    */
    @Override
    public boolean changePassword(NewPasswordUser newPassword, UserAuth userDetails) {
        User user = userDetails.getUser().orElseThrow(NotFoundUserException::new);
        if (encoder.matches(newPassword.getCurrentPassword(), userDetails.getPassword())) {
            UserDetails updateUserDetails = org.springframework.security.core.userdetails.User.builder()
                    .passwordEncoder(this.encoder::encode)
                    .password(newPassword.getNewPassword())
                    .username(user.getEmail())
                    .roles(user.getRole().name())
                    .build();
            user.setPassword(updateUserDetails.getPassword());
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Provides data about the current user.
     */
    @Override
    public UserDto getUserDto(UserAuth userDetails) {
        User user = userDetails.getUser().orElseThrow(NotFoundUserException::new);
        return userMapper.mapToUserDto(user);
    }

    /**
     * Change data about the current user.
     */
    @Override
    public UserChangeDto putUser(UserChangeDto userChange, UserAuth userDetails) {
        User user = userMapper.mapToUserForUserChangeDto(
                userChange,
                userDetails.getUser().orElseThrow(NotFoundUserException::new));
        userRepository.save(user);
        return userChange;
    }

    /**
     * Update the user's image.
     */
    @Override
    public void putUserImage(MultipartFile image, UserAuth userDetails) throws IOException {
        User user = userDetails.getUser().orElseThrow(NotFoundUserException::new);
        if (user.getImage() == null || user.getImage().isEmpty()) {
            user.setImage(WorkWithImage.saveAndGetStringImage(imagePath, user.toString(), image));
        } else {
            user.setImage(WorkWithImage.updateAndGetStringImage(imagePath, user.getImage(), image));
        }
        userRepository.save(user);
    }
}
