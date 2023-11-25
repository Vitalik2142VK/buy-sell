package ru.skypro.homework.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.TestContainerPostgre;
import ru.skypro.homework.component.UserAuth;
import ru.skypro.homework.component.UserAuthDetailsService;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;
import ru.skypro.homework.dto.announce.AnnouncesDtoOut;
import ru.skypro.homework.dto.announce.CreateOrUpdateAd;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundCommentException;
import ru.skypro.homework.mapping.AnnounceMapper;
import ru.skypro.homework.repository.AnnounceRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AnnounceService;


import java.nio.charset.StandardCharsets;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.helper.Helper.*;

public class AnnounceControllerTest extends TestContainerPostgre {
    @Autowired
    private AnnounceRepository announceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AnnounceMapper announceMapper;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserAuthDetailsService userDetailsService;
    @Autowired
    private AnnounceService announceService;
    @Autowired
    private AnnounceController announceController;

    @Test
    @Transactional
    public void getAllAnnounceTest() throws Exception {
        insertUsers(userRepository, encoder);
        insertAnnounce(announceRepository, userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow());
        announceRepository.save(createAnnounce(
                userRepository.findFirstByEmail("ivanov@gmail.com").orElseThrow(),
                "Описание объявления 2",
                "null",
                1500,
                "Заголовок объявления 2"));

        AnnouncesDtoOut dto = announceService.getAll();
        List<AnnounceDtoOut> answer = dto.getResults();

        mockMvc.perform(
                        get("/ads")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(dto.getCount()))
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results[0].author").value(answer.get(0).getAuthor()))
                .andExpect(jsonPath("$.results[0].image").value(answer.get(0).getImage()))
                .andExpect(jsonPath("$.results[0].pk").value(answer.get(0).getPk()))
                .andExpect(jsonPath("$.results[0].price").value(answer.get(0).getPrice()))
                .andExpect(jsonPath("$.results[0].title").value(answer.get(0).getTitle()))
                .andExpect(jsonPath("$.results[1].author").value(answer.get(1).getAuthor()))
                .andExpect(jsonPath("$.results[1].image").value(answer.get(1).getImage()))
                .andExpect(jsonPath("$.results[1].pk").value(answer.get(1).getPk()))
                .andExpect(jsonPath("$.results[1].price").value(1500))
                .andExpect(jsonPath("$.results[1].title").value(answer.get(1).getTitle()));
    }

    @Test
    @Transactional
    void deleteAnnounceTest() throws Exception {
        insertUsers(userRepository, encoder);
        User admin = userRepository.save(createUser(
                "admin@gmail.com",
                "0102030405",
                "Админ",
                "Админов",
                "+78001111111",
                null,
                Role.ADMIN,
                encoder
        ));
        Announce ad = announceRepository.save(createAnnounce(
                userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow(),
                "Описание объявления 3",
                "null",
                2000,
                "Заголовок объявления 3"));

        mockMvc.perform(
                        delete("/ads/" + ad.getId())
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("admin@gmail.com", "0102030405", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThrows(NotFoundCommentException.class, () -> announceRepository.findById(ad.getId()).orElseThrow(NotFoundCommentException::new));
    }

    @Test
    @Transactional
    void updateAnnounceTest() throws Exception {
        insertUsers(userRepository, encoder);
        User author = userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow();
        Announce ad = insertAnnounce(announceRepository, author);

        CreateOrUpdateAd dto = new CreateOrUpdateAd();
        dto.setTitle("Измененный заголовок");
        dto.setPrice(2000);
        dto.setDescription("Измененное описание");

        JSONObject body = new JSONObject();
        body.put("title", dto.getTitle());
        body.put("price", dto.getPrice());
        body.put("description", dto.getDescription());

        mockMvc.perform(
                        patch("/ads/" + ad.getId())
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("ivanov@gmail.com", "12345678", StandardCharsets.UTF_8))
                                .content(body.toString())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(ad.getAuthor().getId()))
                .andExpect(jsonPath("$.image").value("/ads/imageAnnounce/null"))
                .andExpect(jsonPath("$.pk").value(ad.getId()))
                .andExpect(jsonPath("$.price").value(2000))
                .andExpect(jsonPath("$.title").value("Измененный заголовок"));

        Announce actual = announceRepository.findById(ad.getId()).orElseThrow();

        assertEquals("Измененный заголовок", actual.getTitle());
        assertEquals(2000, actual.getPrice());
        assertEquals("Измененное описание", actual.getDescription());
    }
}
