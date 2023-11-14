package ru.skypro.homework.helper;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.User;

public class HelperUser {
    private HelperUser() {}

    public static User createUser(int id, String email, String password, String firstName, String lastName, String image, Role role, PasswordEncoder encoder) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .passwordEncoder(encoder::encode)
                .password(password)
                .username(email)
                .roles(role.name())
                .build();

        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setImage(image);
        user.setRole(role);
        return user;
    }
}
