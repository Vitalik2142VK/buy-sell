package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.TestContainerPostgre;
import ru.skypro.homework.component.UserAuthDetailsService;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;
import ru.skypro.homework.dto.announce.AnnouncesDtoOut;
import ru.skypro.homework.mapping.AnnounceMapper;
import ru.skypro.homework.repository.AnnounceRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AnnounceService;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.helper.HelperUser.*;

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
    public void getUserTest() throws Exception {
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
                .andExpect(jsonPath("$.results[1].price").value(answer.get(1).getPrice()))
                .andExpect(jsonPath("$.results[1].title").value(answer.get(1).getTitle()));
    }
}
