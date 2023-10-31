package ru.skypro.homework.mapping;

import org.springframework.stereotype.Component;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.dto.announce.AnnounceDtoIn;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;

@Component
public class AnnounceMapper {
    public AnnounceDtoOut toDTO(Announce announce) {
        AnnounceDtoOut announceDtoOut = new AnnounceDtoOut();
        announceDtoOut.setAuthor(announceDtoOut.getAuthor());
        announceDtoOut.setImage(announce.getImage());
        announceDtoOut.setPk(Math.toIntExact(announce.getPk()));
        announceDtoOut.setPrice(Math.toIntExact(announce.getPrice()));
        announceDtoOut.setTitle(announce.getTitle());
        return announceDtoOut;
    }

    public Announce toEntity(AnnounceDtoIn announceDtoIn) {
        Announce announce = new Announce();
        announce.setPk(announceDtoIn.getPk());
        announce.setAuthorFirstName(announceDtoIn.getAuthorFirstName());
        announce.setAuthorLastName(announceDtoIn.getAuthorLastName());
        announce.setDescription(announceDtoIn.getDescription());
        announce.setEmail(announceDtoIn.getEmail());
        announce.setImage(announceDtoIn.getImage());
        announce.setPhone(announceDtoIn.getPhone());
        announce.setPrice(announceDtoIn.getPrice());
        announce.setTitle(announceDtoIn.getTitle());
        return announce;
    }
}
