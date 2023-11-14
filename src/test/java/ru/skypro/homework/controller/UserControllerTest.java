package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skypro.homework.component.UserAuthDetailsService;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.mapping.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.helper.HelperUser.createUser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = {UserControllerTest.Initializer.class})
public class UserControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
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

    private org.springframework.security.core.userdetails.User loggedUser;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("mdb")
            .withUsername("mu")
            .withPassword("mp");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password" + postgreSQLContainer.getPassword(),
                    "spring.liquibase.enabled=true"
            ).applyTo(applicationContext.getEnvironment());
        }
    }


    @Test
    @WithUserDetails("petrov@gmail.com")
    public void getUserTest() throws Exception {
        insertUsers();

        loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        UserDetails userDetails = userDetailsService.loadUserByUsername("petrov@gmail.com");
        UserDto dto = userService.getUserDto(userDetails);

        mockMvc.perform(
                get("/users/me")
                        .with(user(loggedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.firstName").value(dto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(dto.getLastName()))
                .andExpect(jsonPath("$.phone").value(dto.getPhone()))
                .andExpect(jsonPath("$.role").value(dto.getRole().name()))
                .andExpect(jsonPath("$.image").value(dto.getImage()));

//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/users/me")
//                        .content(user(loggedUser))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(dto.getId()))
//                .andExpect(jsonPath("$.email").value(dto.getEmail()))
//                .andExpect(jsonPath("$.firstName").value(dto.getFirstName()))
//                .andExpect(jsonPath("$.lastName").value(dto.getLastName()))
//                .andExpect(jsonPath("$.phone").value(dto.getPhone()))
//                .andExpect(jsonPath("$.role").value(dto.getRole().name()))
//                .andExpect(jsonPath("$.image").value(dto.getImage()));

//                "email": "string",
//                "firstName": "string",
//                "lastName": "string",
//                "phone": "string",
//                "role": "USER",
//                "image": "string"

    }

    private void insertUsers() {
        userRepository.save(createUser(1,
                "ivanov@gmail.com",
                "12345678",
                "Иван",
                "Иванов",
                null,
                Role.USER,
                encoder));
        userRepository.save(createUser(2,
                "petrov@gmail.com",
                "87654321",
                "Петр",
                "Петров",
                null,
                Role.USER,
                encoder));
    }
}
