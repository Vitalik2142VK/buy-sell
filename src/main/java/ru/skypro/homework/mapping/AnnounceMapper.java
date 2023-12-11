package ru.skypro.homework.mapping;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.announce.AnnouncesDtoOut;
import ru.skypro.homework.dto.announce.CreateOrUpdateAd;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.dto.announce.AnnounceDtoIn;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;
import ru.skypro.homework.entity.User;
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

    /**
     *
     * the method fills some of the fields of the AnnounceDtoIn object
     */
    public AnnounceDtoIn toDtoIn(Announce announce, User user) {
        AnnounceDtoIn announceDtoIn = new AnnounceDtoIn();
        announceDtoIn.setDescription(announce.getDescription());
        announceDtoIn.setImage(urlImage.getAdsImageUrl(announce.getImage()));
        announceDtoIn.setPrice(announce.getPrice());
        announceDtoIn.setTitle(announce.getTitle());
        announceDtoIn.setPk(user.getId());
        announceDtoIn.setAuthorFirstName(user.getFirstName());
        announceDtoIn.setAuthorLastName(user.getLastName());
        announceDtoIn.setEmail(user.getEmail());
        announceDtoIn.setPhone(user.getPhone());
        return announceDtoIn;
    }

    /**
     * the method fills some of the fields of the AnnounceDtoIn object for update method
     */
    public Announce toAnnounce(CreateOrUpdateAd properties, Announce announce) {
        announce.setDescription(properties.getDescription());
        announce.setPrice(properties.getPrice());
        announce.setTitle(properties.getTitle());
        return announce;
    }

    /**
     * the method fills some of the fields of the AnnounceDtoIn object for add method
     */
    public Announce toAnnounce(CreateOrUpdateAd properties, User user, String work) {
        Announce announce = new Announce();
        announce.setAuthor(user);
        announce.setImage(work);
        announce.setDescription(properties.getDescription());
        announce.setPrice(properties.getPrice());
        announce.setTitle(properties.getTitle());
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
