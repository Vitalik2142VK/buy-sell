package ru.skypro.homework.mapping;

import org.springframework.stereotype.Component;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.dto.announce.AnnounceDtoIn;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundUserException;
import ru.skypro.homework.repository.UserRepository;

@Component
public class AnnounceMapper {
    private final UserRepository userRepository;

    public AnnounceMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AnnounceDtoOut toDTO(Announce announce) {
        AnnounceDtoOut announceDtoOut = new AnnounceDtoOut();
        announceDtoOut.setAuthor(announce.getAuthor().getId());
        announceDtoOut.setImage(announce.getImage());
        announceDtoOut.setPk(announce.getId());
        announceDtoOut.setPrice(announce.getPrice());
        announceDtoOut.setTitle(announce.getTitle());
        return announceDtoOut;
    }

    public Announce toEntity(AnnounceDtoIn announceDtoIn) {
        Announce announce = new Announce();
        announce.setAuthor(userRepository.findFirstByEmail(announceDtoIn.getEmail()).orElseThrow(NotFoundUserException::new));
        announce.setDescription(announceDtoIn.getDescription());
        announce.setImage(announceDtoIn.getImage());
        announce.setPrice(announceDtoIn.getPrice());
        announce.setTitle(announceDtoIn.getTitle());
        return announce;
    }
}
