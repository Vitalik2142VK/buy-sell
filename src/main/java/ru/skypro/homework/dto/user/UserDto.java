package ru.skypro.homework.dto.user;

import lombok.Data;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.User;

@Data
public class UserDto {

    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.role = user.getRole();
        this.image = user.getImage();
    }
}
