package ru.skypro.homework.mapping;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.announce.CreateOrUpdateAd;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.dto.announce.AnnounceDto;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;
import ru.skypro.homework.entity.User;

@Component
public class AnnounceMapper {
    public AnnounceDtoOut toDTO(Announce announce) {
        AnnounceDtoOut announceDtoOut = new AnnounceDtoOut();
        announceDtoOut.setAuthor(announce.getAuthor().getId());
        announceDtoOut.setImage(announce.getImage());
        announceDtoOut.setPk(announce.getId());
        announceDtoOut.setPrice(announce.getPrice());
        announceDtoOut.setTitle(announce.getTitle());
        return announceDtoOut;
    }

    public AnnounceDto mapToAnnounceDto(Announce announce) {
        AnnounceDto dto = new AnnounceDto();
        dto.setPk(announce.getId());
        dto.setAuthorFirstName(announce.getAuthor().getFirstName());
        dto.setAuthorLastName(announce.getAuthor().getLastName());
        dto.setDescription(announce.getDescription());
        dto.setEmail(announce.getAuthor().getEmail());
        dto.setImage(announce.getImage());
        dto.setPhone(announce.getAuthor().getPhone());
        dto.setPrice(announce.getPrice());
        dto.setTitle(announce.getTitle());
        return dto;
    }

    public Announce createdAd(CreateOrUpdateAd dto, String image, User author) {
        Announce announce = new Announce();
        announce.setAuthor(author);
        announce.setImage(image);
        announce.setDescription(dto.getDescription());
        announce.setPrice(dto.getPrice());
        announce.setTitle(dto.getTitle());
        return announce;
    }
}
