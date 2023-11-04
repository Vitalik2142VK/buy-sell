package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.announce.AnnounceDto;
import ru.skypro.homework.dto.announce.CreateOrUpdateAd;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;

import java.io.IOException;
import java.util.List;

public interface AnnounceService {
    List<AnnounceDtoOut> getUsersAll();
    List<AnnounceDtoOut> getAll();
    AnnounceDto get(Integer id);
    AnnounceDtoOut add(CreateOrUpdateAd properties, MultipartFile image) throws IOException;
    AnnounceDtoOut updateInfo(Integer id, CreateOrUpdateAd property);
    void updateImage(Integer id, MultipartFile image) throws IOException;
    void delete(Integer id);
}
