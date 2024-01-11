package ru.skypro.homework.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.TestContainerPostgre;
import ru.skypro.homework.component.UserAuthDetailsService;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.util.Helper.insertUsers;

public class AuthControllerTest extends TestContainerPostgre {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthDetailsService detailsService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthController authController;

    @Test
    @Transactional
    public void loginTest() throws Exception {
        insertUsers(userRepository, encoder);

        Login dto = new Login();
        dto.setUsername("ivanov@gmail.com");
        dto.setPassword("12345678");

        JSONObject body = new JSONObject();
        body.put("username", dto.getUsername());
        body.put("password", dto.getPassword());

        mockMvc.perform(
                        post("/login")
                                .content(body.toString())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void registerTest() throws Exception {
        Register dto = new Register();
        dto.setUsername("admin@test.ru");
        dto.setPassword("1234567890");
        dto.setFirstName("Админ");
        dto.setLastName("Админов");
        dto.setPhone("+79999999999");
        dto.setRole(Role.ADMIN);

        JSONObject body = new JSONObject();
        body.put("username", dto.getUsername());
        body.put("password", dto.getPassword());
        body.put("firstName", dto.getFirstName());
        body.put("lastName", dto.getLastName());
        body.put("phone", dto.getPhone());
        body.put("role", dto.getRole().name());

        mockMvc.perform(
                        post("/register")
                                .content(body.toString())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        User user = userRepository.findFirstByEmail("admin@test.ru").orElseThrow();

        assertEquals("admin@test.ru", user.getEmail());
        assertTrue(encoder.matches("1234567890", user.getPassword()));
        assertEquals("Админ", user.getFirstName());
        assertEquals("Админов", user.getLastName());
        assertEquals("+79999999999", user.getPhone());
        assertEquals(Role.ADMIN, user.getRole());
    }
}
