package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.TestContainerPostgre;
import ru.skypro.homework.component.UserAuth;
import ru.skypro.homework.component.UserAuthDetailsService;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.mapping.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.helper.Helper.insertUsers;

public class UserControllerTest extends TestContainerPostgre{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserAuthDetailsService userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserController userController;

    @Test
    @Transactional
    public void getAllTest() throws Exception {
        insertUsers(userRepository, encoder);

        UserAuth userDetails = (UserAuth) userDetailsService.loadUserByUsername("ivanov@gmail.com");
        UserDto dto = userService.getUserDto(userDetails);

        mockMvc.perform(
                get("/users/me")
                        .header(HttpHeaders.AUTHORIZATION,"Basic " + HttpHeaders.encodeBasicAuth("ivanov@gmail.com", "12345678", StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.firstName").value(dto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(dto.getLastName()))
                .andExpect(jsonPath("$.phone").value(dto.getPhone()))
                .andExpect(jsonPath("$.role").value(dto.getRole().name()))
                .andExpect(jsonPath("$.image").value(dto.getImage()));
    }
}
