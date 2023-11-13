package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.announce.AnnounceDtoIn;
import ru.skypro.homework.dto.announce.AnnouncesDtoOut;
import ru.skypro.homework.dto.announce.CreateOrUpdateAd;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;

import java.io.IOException;
import java.util.List;

public interface AnnounceService {
    AnnouncesDtoOut getAll();
    List<AnnounceDtoOut> getAllOfUser(String email);
    AnnounceDtoIn get(Integer id);
    AnnounceDtoOut add(CreateOrUpdateAd properties, MultipartFile image, String email) throws IOException;
    AnnounceDtoOut updateInfo(Integer id, CreateOrUpdateAd property);
    void updateImage(Integer id, MultipartFile image) throws IOException;
    void delete(Integer id);
}
