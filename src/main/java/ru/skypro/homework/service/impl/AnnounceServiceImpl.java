package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.announce.Announce;
import ru.skypro.homework.dto.announce.AnnounceOut;
import ru.skypro.homework.dto.announce.Property;
import ru.skypro.homework.service.AnnounceService;

import java.util.List;

@Service
public class AnnounceServiceImpl implements AnnounceService {
    @Override
    public List<AnnounceOut> getUsersAll() {
        return null;
    }

    @Override
    public List<AnnounceOut> getAll() {
        return null;
    }

    @Override
    public Announce get(Long id) {
        return null;
    }

    @Override
    public AnnounceOut add(Property properties, MultipartFile image) {
        return null;
    }

    @Override
    public AnnounceOut updateInfo(Long id, Property property) {
        return null;
    }

    @Override
    public void updateImage(Long id, MultipartFile image) {

    }

    @Override
    public void delete(Long id) {

    }
}
