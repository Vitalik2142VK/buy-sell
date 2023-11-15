package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.webservices.server.AutoConfigureMockWebServiceClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.config.WebSecurityConfig;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapping.AnnounceMapper;
import ru.skypro.homework.mapping.CommentMapper;
import ru.skypro.homework.mapping.UserMapper;
import ru.skypro.homework.repository.AnnounceRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AnnounceServiceImpl;
import ru.skypro.homework.service.impl.AuthServiceImpl;
import ru.skypro.homework.service.impl.CommentServiceImpl;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {WebSecurityConfig.class})
@AutoConfigureMockWebServiceClient
//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = InMemoryUserDetailsManager.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AnnounceRepository announceRepository;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AuthController authController;
    @Autowired
    private AuthServiceImpl authService;
    @MockBean
    private AnnounceServiceImpl announceService;
    @MockBean
    private CommentServiceImpl commentService;
    @Autowired
    private Register register;
    @Autowired
    private Login login;
//    @MockBean
//    WebSecurityConfig config;
//

    private CommentController commentController;


//    @BeforeEach
//    public void setUp() {
//        User user = new User();
//        User authorComment = new User();
//        Announce announce = new Announce();
//        Comment comment = new Comment();
//
//        user.setId(1);
//        user.setFirstName("Sergey");
//        authorComment.setFirstName("Anonymous");
//
//        announce.setId(2);
//        announce.setAuthor(user);
//        announce.setTitle("Bicycle");
//        announce.setPrice(9999);
//
//        comment.setId(3);
//        comment.setAuthor(authorComment);
//        comment.setAd(announce);
//        comment.setText("Very good bicycle!");
//    }

    @Test
    void findAllAdCommentsTest() throws Exception {

        String text = "Very good bicycle!";
        int id = 3;

        JSONObject commentObject = new JSONObject();
        commentObject.put("text", text);

        User user = new User();
        User authorComment = new User();
        Announce announce = new Announce();
        Comment comment = new Comment();

        user.setId(1);
        user.setFirstName("Sergey");
        user.setRole(Role.ADMIN);

        authorComment.setId(11);
        authorComment.setFirstName("Anonymous");
        authorComment.setRole(Role.USER);

        announce.setId(2);
        announce.setAuthor(user);
        announce.setTitle("Bicycle");
        announce.setPrice(9999);

        comment.setId(id);
        comment.setAuthor(authorComment);
        comment.setAd(announce);
        comment.setText(text);

        List<Comment> list = List.of(comment);
        List<User> users = List.of(user, authorComment);



//        Mockito.when(userRepository.findAll()).thenReturn(List.of(user, authorComment));
        Mockito.when(announceRepository.findById(Math.toIntExact(any(Long.class)))).thenReturn(Optional.of(announce));
        Mockito.when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        Mockito.when(commentRepository.findAllByAd_IdOrderByCreatedAtDesc(Math.toIntExact(any(Long.class)))).
                thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:3000/ads/2/comments"))
//        mockMvc.perform(get("/2/comments"))
//                        .content(list.toString())
//                        .content(commentObject.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.text").value(text));

    }

    @Test
    void createCommentTest() {
    }

    @Test
    void deleteAdComment() {
    }

    @Test
    void updateComment() {
    }
}