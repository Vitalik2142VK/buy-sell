package ru.skypro.homework.mapping;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.announce.AnnouncesDtoOut;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.dto.announce.AnnounceDtoIn;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;
import ru.skypro.homework.exception.NotFoundUserException;
import ru.skypro.homework.helper.WorkImagePathAndUrl;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnnounceMapper {
    private final UserRepository userRepository;
    private final WorkImagePathAndUrl urlImage;

    public AnnounceMapper(UserRepository userRepository, WorkImagePathAndUrl urlImage) {
        this.userRepository = userRepository;
        this.urlImage = urlImage;
    }

    public AnnounceDtoOut toDTO(Announce announce) {
        AnnounceDtoOut announceDtoOut = new AnnounceDtoOut();
        announceDtoOut.setAuthor(announce.getAuthor().getId());
        announceDtoOut.setImage(urlImage.getAdsImageUrl(announce.getImage()));
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

    /**
     * A method that converts a collection of the Announce class to a collection of the AnnounceDtoOut class.
     */
    public AnnouncesDtoOut announceListToAnnounceDtoOutList(List<Announce> announces) {
        var announcesDtoOut = new AnnouncesDtoOut();
        announcesDtoOut.setCount(announces.size());

        var announceDtoOutList = announces
                .stream()
                .map(this::toDTO).collect(Collectors.toList());
        announcesDtoOut.setResults(announceDtoOutList);
        return announcesDtoOut;
    }
}
