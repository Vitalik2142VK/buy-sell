package ru.skypro.homework.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.TestContainerPostgre;
import ru.skypro.homework.component.UserAuth;
import ru.skypro.homework.component.UserAuthDetailsService;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.user.NewPasswordUser;
import ru.skypro.homework.dto.user.UserChangeDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.helper.WorkImagePathAndUrl;
import ru.skypro.homework.mapping.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.helper.Helper.createUser;
import static ru.skypro.homework.helper.Helper.insertUsers;

public class UserControllerTest extends TestContainerPostgre{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkImagePathAndUrl workImagePathAndUrl;
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
    public void setPasswordTest() throws Exception {
        insertUsers(userRepository, encoder);

        NewPasswordUser newPassword = new NewPasswordUser();
        newPassword.setCurrentPassword("12345678");
        newPassword.setNewPassword("10101010");

        JSONObject body = new JSONObject();
        body.put("currentPassword", newPassword.getCurrentPassword());
        body.put("newPassword", newPassword.getNewPassword());

        mockMvc.perform(
                        post("/users/set_password")
                                .header(HttpHeaders.AUTHORIZATION,"Basic " + HttpHeaders.encodeBasicAuth("ivanov@gmail.com", "12345678", StandardCharsets.UTF_8))
                                .content(body.toString())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User actual = userRepository.findFirstByEmail("ivanov@gmail.com").orElseThrow();

        assertTrue(encoder.matches("10101010", actual.getPassword()));
    }

    @Test
    @Transactional
    public void getUserTest() throws Exception {
        insertUsers(userRepository, encoder);

        UserAuth userDetails = (UserAuth) userDetailsService.loadUserByUsername("petrov@gmail.com");
        UserDto dto = userService.getUserDto(userDetails);

        mockMvc.perform(
                get("/users/me")
                        .header(HttpHeaders.AUTHORIZATION,"Basic " + HttpHeaders.encodeBasicAuth("petrov@gmail.com", "87654321", StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.email").value("petrov@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Петр"))
                .andExpect(jsonPath("$.lastName").value("Петров"))
                .andExpect(jsonPath("$.phone").value("+78002222222"))
                .andExpect(jsonPath("$.role").value(Role.USER.name()))
                .andExpect(jsonPath("$.image").value(dto.getImage()));
    }

    @Test
    @Transactional
    public void putUserTest() throws Exception {
        insertUsers(userRepository, encoder);

        UserChangeDto dto = new UserChangeDto();
        dto.setFirstName("Tомас");
        dto.setLastName("Эдисон");
        dto.setPhone("+79808808080");

        JSONObject body = new JSONObject();
        body.put("firstName", dto.getFirstName());
        body.put("lastName", dto.getLastName());
        body.put("phone", dto.getPhone());

        mockMvc.perform(
                        patch("/users/me")
                                .header(HttpHeaders.AUTHORIZATION,"Basic " + HttpHeaders.encodeBasicAuth("petrov@gmail.com", "87654321", StandardCharsets.UTF_8))
                                .content(body.toString())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Tомас"))
                .andExpect(jsonPath("$.lastName").value("Эдисон"))
                .andExpect(jsonPath("$.phone").value("+79808808080"));

        User actual = userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow();

        assertEquals("Tомас", actual.getFirstName());
        assertEquals("Эдисон", actual.getLastName());
        assertEquals("+79808808080", actual.getPhone());
    }
}
