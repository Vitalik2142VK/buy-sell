package ru.skypro.homework.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.TestContainerPostgre;
import ru.skypro.homework.component.UserAuth;
import ru.skypro.homework.component.UserAuthDetailsService;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.announce.AnnounceDtoIn;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;
import ru.skypro.homework.dto.announce.AnnouncesDtoOut;
import ru.skypro.homework.dto.announce.CreateOrUpdateAd;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundCommentException;
import ru.skypro.homework.util.WorkImagePathAndUrl;
import ru.skypro.homework.mapping.AnnounceMapper;
import ru.skypro.homework.repository.AnnounceRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AnnounceService;


import java.nio.charset.StandardCharsets;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.util.Helper.*;

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
    private WorkImagePathAndUrl workImagePathAndUrl;
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
                .andExpect(jsonPath("$.results[0].price").value(1000))
                .andExpect(jsonPath("$.results[0].title").value("Заголовок объявления"))
                .andExpect(jsonPath("$.results[1].author").value(answer.get(1).getAuthor()))
                .andExpect(jsonPath("$.results[1].image").value(answer.get(1).getImage()))
                .andExpect(jsonPath("$.results[1].pk").value(answer.get(1).getPk()))
                .andExpect(jsonPath("$.results[1].price").value(1500))
                .andExpect(jsonPath("$.results[1].title").value("Заголовок объявления 2"));
    }

    @Test
    @Transactional
    public void getAllOfUserTest() throws Exception {
        insertUsers(userRepository, encoder);
        insertAnnounce(announceRepository, userRepository.findFirstByEmail("petrov@gmail.com").orElseThrow());
        announceRepository.save(createAnnounce(
                userRepository.findFirstByEmail("ivanov@gmail.com").orElseThrow(),
                "Описание объявления 2",
                "null",
                1500,
                "Заголовок объявления 2"));

        UserAuth userDetails = (UserAuth) userDetailsService.loadUserByUsername("ivanov@gmail.com");
        AnnouncesDtoOut dto = announceService.getAllOfUser(userDetails);
        List<AnnounceDtoOut> answer = dto.getResults();

        mockMvc.perform(
                        get("/ads/me")
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("ivanov@gmail.com", "12345678", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(dto.getCount()))
                .andExpect(jsonPath("$.results", hasSize(1)))
                .andExpect(jsonPath("$.results[0].author").value(answer.get(0).getAuthor()))
                .andExpect(jsonPath("$.results[0].image").value(answer.get(0).getImage()))
                .andExpect(jsonPath("$.results[0].pk").value(answer.get(0).getPk()))
                .andExpect(jsonPath("$.results[0].price").value(1500))
                .andExpect(jsonPath("$.results[0].title").value("Заголовок объявления 2"));
    }

    @Test
    @Transactional
    public void addTest() throws Exception {
        User author = userRepository.save(createUser(
                "petrov@gmail.com",
                "87654321",
                "Петр",
                "Петров",
                "+78002222222",
                null,
                Role.USER,
                encoder));


        CreateOrUpdateAd dto = new CreateOrUpdateAd();
        dto.setTitle("Заголовок объявления");
        dto.setPrice(1500);
        dto.setDescription("Описание объявления");

        JSONObject body = new JSONObject();
        body.put("title", "Заголовок объявления");
        body.put("price", 1500);
        body.put("description", "Описание объявления");

        int numberAds = announceRepository.getNumberUserAds(author.getId()) + 1;
        String imageExtend = "Ads_" + numberAds + "_auth_" + author.getId() + "_lg_" + author.getEmail().hashCode();

        String stringJson = "{  \"title\": \"Заголовок объявления\",\"price\": 1500,\"description\": \"Описание объявления\"}";//body.toString(); //"{\"title\":\"Заголовок объявления\",\"price\":1500,\"description\":\"Описание объявления\"}";

        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "1234".getBytes());
        MockMultipartFile json = new MockMultipartFile("json", "", MediaType.APPLICATION_JSON_VALUE, stringJson.getBytes());

        mockMvc.perform(multipart(HttpMethod.POST, "/ads")
                        .file(json)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("petrov@gmail.com", "87654321", StandardCharsets.UTF_8)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(author.getId()))
                .andExpect(jsonPath("$.image").value("/ads/imageAnnounce/" + imageExtend))
                .andExpect(jsonPath("$.pk").isNotEmpty())
                .andExpect(jsonPath("$.price").value(1500))
                .andExpect(jsonPath("$.title").value("Заголовок объявления"));

    }

    @Test
    @Transactional
    void getTest() throws Exception {
        User author = userRepository.save(createUser(
                "ivanov@gmail.com",
                "12345678",
                "Иван",
                "Иванов",
                "+78001111111",
                null,
                Role.USER,
                encoder
        ));
        Announce ad = insertAnnounce(announceRepository, author);

        AnnounceDtoIn dto = announceService.get(ad.getId());
        String imageAds = "/ads/imageAnnounce/null";

        mockMvc.perform(
                        get("/ads/" + ad.getId())
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("ivanov@gmail.com", "12345678", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(author.getId()))
                .andExpect(jsonPath("$.authorFirstName").value("Иван"))
                .andExpect(jsonPath("$.authorLastName").value("Иванов"))
                .andExpect(jsonPath("$.description").value("Описание объявления"))
                .andExpect(jsonPath("$.email").value("ivanov@gmail.com"))
                .andExpect(jsonPath("$.image").value(imageAds))
                .andExpect(jsonPath("$.phone").value("+78001111111"))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.title").value("Заголовок объявления"));
    }

    @Test
    @Transactional
    void deleteAnnounceTest() throws Exception {
        insertUsers(userRepository, encoder);
        userRepository.save(createUser(
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

    @Test
    @Transactional
    public void updateImageTest() throws Exception {
        User author = userRepository.save(createUser(
                "petrov@gmail.com",
                "87654321",
                "Петр",
                "Петров",
                "+78002222222",
                null,
                Role.USER,
                encoder));
        Announce ad = announceRepository.save(createAnnounce(
                author,
                "Описание объявления",
                "image.jpg",
                1000,
                "Заголовок объявления"
        ));

        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "1234".getBytes());

        mockMvc.perform(multipart(HttpMethod.PATCH ,"/ads/" + ad.getId() + "/image")
                        .file(image)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("petrov@gmail.com", "87654321", StandardCharsets.UTF_8)))
                .andExpect(status().isOk());

        byte[] actual = announceService.getImage(
                announceRepository.findById(ad.getId())
                        .orElseThrow()
                        .getImage());

        assertArrayEquals("1234".getBytes(), actual);
    }

    @Test
    @Transactional
    public void getImageTest() throws Exception {
        User author = userRepository.save(createUser(
                "petrov@gmail.com",
                "87654321",
                "Петр",
                "Петров",
                "+78002222222",
                null,
                Role.USER,
                encoder));
        Announce ad = announceRepository.save(createAnnounce(
                author,
                "Описание объявления",
                "image.jpg",
                1000,
                "Заголовок объявления"
        ));

        MultipartFile image = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image".getBytes());
        announceService.updateImage(ad.getId() ,image);

        mockMvc.perform(
                        get(workImagePathAndUrl.getAdsImageUrl(ad.getImage()))
                                .header(HttpHeaders.AUTHORIZATION,"Basic " + HttpHeaders.encodeBasicAuth("petrov@gmail.com", "87654321", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        byte[] actual = announceService.getImage(
                announceRepository.findById(ad.getId())
                        .orElseThrow()
                        .getImage());

        assertArrayEquals("image".getBytes(), actual);
    }
}
