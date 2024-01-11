package ru.skypro.homework.mapping;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.UserAuth;
import ru.skypro.homework.dto.announce.AnnouncesDtoOut;
import ru.skypro.homework.dto.announce.CreateOrUpdateAd;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.dto.announce.AnnounceDtoIn;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.util.WorkImagePathAndUrl;
import ru.skypro.homework.service.impl.AnnounceServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnnounceMapper {
    private final WorkImagePathAndUrl urlImage;

    public AnnounceMapper(WorkImagePathAndUrl urlImage) {
        this.urlImage = urlImage;
    }

    /**
     * Converts {@link Announce} into {@link AnnounceDtoOut}<br>
     * It is used in methods:
     * <br> - {@link AnnounceServiceImpl#add(CreateOrUpdateAd, MultipartFile, UserAuth)}
     * <br> - {@link AnnounceServiceImpl#updateInfo(Integer, CreateOrUpdateAd)}
     */
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
     * Converts {@link Announce} into {@link AnnounceDtoIn}<br>
     * It is used in methods:
     * <br> - {@link AnnounceServiceImpl#get(Integer)}
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
     * Update {@link Announce} from {@link CreateOrUpdateAd}<br>
     * It is used in methods:
     * <br> - {@link AnnounceServiceImpl#updateInfo(Integer, CreateOrUpdateAd)}
     */
    public Announce toAnnounce(CreateOrUpdateAd properties, Announce announce) {
        announce.setDescription(properties.getDescription());
        announce.setPrice(properties.getPrice());
        announce.setTitle(properties.getTitle());
        return announce;
    }

    /**
     * Creates {@link Announce} from {@link CreateOrUpdateAd}<br>
     * It is used in methods:
     * <br> - {@link AnnounceServiceImpl#add(CreateOrUpdateAd, MultipartFile, UserAuth)}
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
     * Converts {@link List<Announce>} into {@link AnnouncesDtoOut}<br>
     * It is used in methods:
     * <br> - {@link AnnounceServiceImpl#getAll()}
     * <br> - {@link AnnounceServiceImpl#getImage(String)}
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
