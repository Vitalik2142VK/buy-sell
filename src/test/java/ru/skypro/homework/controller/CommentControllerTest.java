package ru.skypro.homework.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import ru.skypro.homework.TestContainerPostgre;
import ru.skypro.homework.component.UserAuth;
import ru.skypro.homework.component.UserAuthDetailsService;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundCommentException;
import ru.skypro.homework.mapping.CommentMapper;
import ru.skypro.homework.repository.AnnounceRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.helper.Helper.*;

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
                        get("/ads/" + ad.getId() + "/comments")
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("ivanov@gmail.com", "12345678", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results", hasSize(1)))
                .andExpect(jsonPath("$.results[0].author").value(answer.getAuthor()))
                .andExpect(jsonPath("$.results[0].authorImage").value(answer.getAuthorImage()))
                .andExpect(jsonPath("$.results[0].authorFirstName").value("Петр"))
                .andExpect(jsonPath("$.results[0].createdAt").value(answer.getCreatedAt()))
                .andExpect(jsonPath("$.results[0].pk").value(answer.getPk()))
                .andExpect(jsonPath("$.results[0].text").value("Текст комментария"));
    }

    @Test
    @Transactional
    void createCommentTest() throws Exception {
        insertUsers(userRepository, encoder);
        User author = userRepository.findFirstByEmail("ivanov@gmail.com").orElseThrow();
        Announce ad = announceRepository.save(createAnnounce(
                userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow(),
                "Описание объявления 3",
                "null",
                2000,
                "Заголовок объявления 3"));

        String text = "Good product!";
        commentRepository.save(createComment(author, ad, text));
        insertComment(commentRepository, ad, author);

        CommentsDto dto = commentService.findAllAdComments(ad.getId());
        CommentDto answer = dto.getResults().get(0);

        JSONObject body = new JSONObject();
        body.put("text", text);

        mockMvc.perform(
                        post("/ads/" + ad.getId()+ "/comments")
                                .header(HttpHeaders.AUTHORIZATION,"Basic " + HttpHeaders.encodeBasicAuth("ivanov@gmail.com", "12345678", StandardCharsets.UTF_8))
                                .content(body.toString())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(answer.getAuthor()))
                .andExpect(jsonPath("$.authorImage").value(answer.getAuthorImage()))
                .andExpect(jsonPath("$.authorFirstName").value("Иван"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.pk").isNotEmpty())
                .andExpect(jsonPath("$.text").value("Good product!"));
    }

    @Test
    @Transactional
    void deleteAdComment() throws Exception{
        insertUsers(userRepository, encoder);
        insertAnnounce(announceRepository, userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow());
        User author = userRepository.findFirstByEmail("ivanov@gmail.com").orElseThrow();
        Announce ad = announceRepository.save(createAnnounce(
                userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow(),
                "Описание объявления 3",
                "null",
                2000,
                "Заголовок объявления 3"));
        String text = "Good product!";

        Comment comment = commentRepository.save(createComment(author, ad, text));

        mockMvc.perform(
                        delete("/ads/" + ad.getId() + "/comments/" + comment.getId())
                                .header(HttpHeaders.AUTHORIZATION,"Basic " + HttpHeaders.encodeBasicAuth("ivanov@gmail.com", "12345678", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThrows(NotFoundCommentException.class, () -> commentRepository.findById(comment.getId()).orElseThrow(NotFoundCommentException::new));
    }

    @Test
    @Transactional
    void deleteAdCommentWhenUnauthorized() throws Exception{
        insertUsers(userRepository, encoder);
            userRepository.save(createUser(
                    "sidorov@gmail.com",
                    "11223344",
                    "Сидр",
                    "Сидоров",
                    "+79185855555",
                    null,
                    Role.USER,
                    encoder
            ));
        insertAnnounce(announceRepository, userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow());
        User author = userRepository.findFirstByEmail("ivanov@gmail.com").orElseThrow();
        Announce ad = announceRepository.save(createAnnounce(
                userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow(),
                "Описание объявления 3",
                "null",
                2000,
                "Заголовок объявления 3"));
        String text = "Good product!";

        Comment comment = commentRepository.save(createComment(author, ad, text));

        mockMvc.perform(
                        delete("/ads/" + ad.getId() + "/comments/" + comment.getId())
                                .header(HttpHeaders.USER_AGENT, "Basic " + HttpHeaders.encodeBasicAuth("sidorov@gmail.com", "11223344", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }
}