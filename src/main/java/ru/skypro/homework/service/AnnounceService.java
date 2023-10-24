package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.announce.AnnounceOut;
import ru.skypro.homework.dto.announce.Announce;
import ru.skypro.homework.dto.announce.CreateOrUpdateAd;

import java.util.List;

public interface AnnounceService {
    List<AnnounceOut> getUsersAll();
    List<AnnounceOut> getAll();
    Announce get(Long id);
    AnnounceOut add(CreateOrUpdateAd properties, MultipartFile image);
    AnnounceOut updateInfo(Long id, CreateOrUpdateAd property);
    void updateImage(Long id, MultipartFile image);
    void delete(Long id);
}
