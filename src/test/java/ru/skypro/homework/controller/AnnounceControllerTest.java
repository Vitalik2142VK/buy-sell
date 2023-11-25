package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.TestContainerPostgre;
import ru.skypro.homework.component.UserAuthDetailsService;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;
import ru.skypro.homework.dto.announce.AnnouncesDtoOut;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapping.AnnounceMapper;
import ru.skypro.homework.repository.AnnounceRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AnnounceService;


import java.nio.charset.StandardCharsets;


import static org.hamcrest.Matchers.hasSize;
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

//    @Test
//    public void getAllAnnounceTest() throws Exception {
//        insertUsers(userRepository, encoder);
//        User author = userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow();
//        Announce ad = announceRepository.save(createAnnounce(
//                author,
//                "Описание объявления",
//                "null",
//                1000,
//                "Заголовок объявления"));
//
//
//        AnnouncesDtoOut dto = announceService.getAll();
//        AnnounceDtoOut dtoOut = dto.getResults().get(0);
//
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/ads")
//                        .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("ivanov@gmail.com", "12345678", StandardCharsets.UTF_8))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$.[0].author").value(dtoOut.getAuthor()))
//                .andExpect(jsonPath("$.[0].description").value(dtoOut.getTitle()));
//    }
//
//    @Test
//    @Transactional
//    void deleteAnnounceTest() throws Exception {
//        insertUsers(userRepository, encoder);
//        insertAnnounce(announceRepository, userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow());
//        Announce ad = announceRepository.save(createAnnounce(
//                userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow(),
//                "Описание объявления 3",
//                "null",
//                2000,
//                "Заголовок объявления 3"));// delete?
//
//        mockMvc.perform(
//                        delete("/ads/" + ad.getId())
//                                .header(HttpHeaders.USER_AGENT, "Basic " + HttpHeaders.encodeBasicAuth("sidorov@gmail.com", "11223344", StandardCharsets.UTF_8))
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isUnauthorized());
//
//    }
//
//    @Test
//    @Transactional
//    void updateAnnounceTest() throws Exception {
//        String title = "Заголовок объявления 3";
//        insertUsers(userRepository, encoder);
//        insertAnnounce(announceRepository, userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow());
//        Announce ad = announceRepository.save(createAnnounce(
//                userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow(),
//                "Описание объявления 3",
//                "null",
//                2000,
//                title));
//
//        String titleNew = "Заголовок объявления 123";
//
//
//        mockMvc.perform(
//                        patch("/ads/" + ad.getId())
//                                .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("ivanov@gmail.com", "12345678", StandardCharsets.UTF_8))
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(titleNew))
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    @Transactional
//    void addAnnounceTest() throws Exception {
//        insertUsers(userRepository, encoder);
//        User author = userRepository.findFirstByEmail("ivanov@gmail.com").orElseThrow();
//        Announce ad = announceRepository.save(createAnnounce(
//                userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow(),
//                "Описание объявления 3",
//                "null",
//                2000,
//                "Заголовок объявления 3"));
//
//        AnnouncesDtoOut dto = announceService.getAll();
//        AnnounceDtoOut dtoOut = dto.getResults().get(0);
//
//
//        mockMvc.perform(
//                        post("/ads/" + ad.getId())
//                                .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("ivanov@gmail.com", "12345678", StandardCharsets.UTF_8))
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.author").value(dtoOut.getAuthor()))
//                .andExpect(jsonPath("$.authorImage").value(dtoOut.getImage()))
//                .andExpect(jsonPath("$.authorFirstName").value("Иван"))
//                .andExpect(jsonPath("$.pk").value(dtoOut.getPk()));
//
//    }
}
