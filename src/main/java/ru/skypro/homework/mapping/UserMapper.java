package ru.skypro.homework.mapping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.user.UserChangeDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.entity.User;

@Component
public class UserMapper {

    @Value("${user.image}")
    String imagePath;

    public UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        if (user.getImage() == null || user.getImage().isEmpty()) {
            dto.setImage(null);
        } else {
            dto.setImage(user.getImage());
        }
        return dto;
    }

    public User mapToUserForUserChangeDto(UserChangeDto dto, User user) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        return user;
    }
}
