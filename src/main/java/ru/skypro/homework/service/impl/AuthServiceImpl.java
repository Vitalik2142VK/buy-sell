package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.component.UserAuthDetailsService;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.exception.UserExistException;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserAuthDetailsService detailsService;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    public AuthServiceImpl(UserAuthDetailsService detailsService,
                           PasswordEncoder passwordEncoder,
                           UserRepository userRepository) {
        this.detailsService = detailsService;
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public boolean login(String userName, String password) {
        UserDetails userDetails = detailsService.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(Register register) {
        ru.skypro.homework.entity.User user = userRepository.findFirstByEmail(register.getUsername()).orElse(null);
        if (user != null) {
            throw new UserExistException();
        }
        UserDetails userDetails = User.builder()
                .passwordEncoder(this.encoder::encode)
                .password(register.getPassword())
                .username(register.getUsername())
                .roles(register.getRole().name())
                .build();

        user = new ru.skypro.homework.entity.User();
        user.setEmail(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setPhone(register.getPhone());
        user.setRole(register.getRole());
        userRepository.save(user);
        //TODO добавить логги о добавлении нового пользователя
        return true;
    }

}
