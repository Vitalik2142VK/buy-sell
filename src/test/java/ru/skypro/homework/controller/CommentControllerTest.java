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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skypro.homework.TestContainerPostgre;
import ru.skypro.homework.component.UserAuthDetailsService;
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
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.impl.AnnounceServiceImpl;
import ru.skypro.homework.service.impl.AuthServiceImpl;
import ru.skypro.homework.service.impl.CommentServiceImpl;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.helper.HelperUser.*;

class CommentControllerTest extends TestContainerPostgre {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnnounceRepository announceRepository;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserAuthDetailsService userDetailsService;
    @Autowired
    private CommentService commentService;
    @Autowired
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
    @Transactional
    void findAllAdCommentsTest() throws Exception {
        insertUsers(userRepository, encoder);
        User author = userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow();
        Announce ad = announceRepository.save(createAnnounce(
                author,
                "Описание объявления",
                "null",
                1000,
                "Заголовок объявления"
                ));
        insertComment(commentRepository, ad, author);

        CommentsDto dto = commentService.findAllAdComments(ad.getId());
        CommentDto answer = dto.getResults().get(0);

        mockMvc.perform(
                        get("/ads/" + ad.getId()+ "/comments")
                                .header(HttpHeaders.AUTHORIZATION,"Basic " + HttpHeaders.encodeBasicAuth("ivanov@gmail.com", "12345678", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(dto.getCount()))
                .andExpect(jsonPath("$.results", hasSize(1)))
                .andExpect(jsonPath("$.results[0].author").value(answer.getAuthor()))
                .andExpect(jsonPath("$.results[0].authorImage").value(answer.getAuthorImage()))
                .andExpect(jsonPath("$.results[0].authorFirstName").value(answer.getAuthorFirstName()))
                .andExpect(jsonPath("$.results[0].createdAt").value(answer.getCreatedAt()))
                .andExpect(jsonPath("$.results[0].pk").value(answer.getPk()))
                .andExpect(jsonPath("$.results[0].text").value(answer.getText()));

    }

//    @Test
//    void createCommentTest() {
//    }
//
//    @Test
//    void deleteAdComment() {
//    }
//
//    @Test
//    void updateComment() {
//    }
}