package ru.skypro.homework.mapping;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.user.UserChangeDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.helper.WorkImagePathAndUrl;

@Component
public class UserMapper {
    private final WorkImagePathAndUrl urlImage;

    public UserMapper(WorkImagePathAndUrl urlImage) {
        this.urlImage = urlImage;
    }

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
            dto.setImage(urlImage.getUserImageUrl(user.getImage()));
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
