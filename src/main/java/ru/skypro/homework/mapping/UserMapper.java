package ru.skypro.homework.mapping;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;

@Component
public class UserMapper {
    private final UserRepository userRepository;

    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto mapToUserDto(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return new UserDto(user);
    }
}
