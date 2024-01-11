package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.UserAuth;
import ru.skypro.homework.dto.announce.AnnounceDtoIn;
import ru.skypro.homework.dto.announce.AnnouncesDtoOut;
import ru.skypro.homework.dto.announce.CreateOrUpdateAd;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;

import java.io.IOException;
import java.util.List;

public interface AnnounceService {
    AnnouncesDtoOut getAll();
    AnnouncesDtoOut getAllOfUser(UserAuth userDetails);
    AnnounceDtoIn get(Integer id);
    AnnounceDtoOut add(CreateOrUpdateAd properties, MultipartFile image, UserAuth userDetails) throws IOException;
    AnnounceDtoOut updateInfo(Integer id, CreateOrUpdateAd property);
    String updateImage(Integer id, MultipartFile image) throws IOException;
    void delete(Integer id) throws IOException;
    byte[] getImage(String nameImage) throws IOException;
}
