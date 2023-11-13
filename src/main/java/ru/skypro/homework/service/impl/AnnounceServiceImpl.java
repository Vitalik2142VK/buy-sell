package ru.skypro.homework.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.announce.CreateOrUpdateAd;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.dto.announce.AnnounceDtoIn;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundAnnounceException;
import ru.skypro.homework.exception.NotFoundUserException;
import ru.skypro.homework.mapping.AnnounceMapper;
import ru.skypro.homework.repository.AnnounceRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AnnounceService;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnnounceServiceImpl implements AnnounceService {
    private final AnnounceRepository announceRepository;
    private final UserRepository userRepository;
    private final AnnounceMapper announceMapper;

    public AnnounceServiceImpl(AnnounceRepository announceRepository, UserRepository userRepository, AnnounceMapper announceMapper) {
        this.userRepository = userRepository;
        this.announceRepository = announceRepository;
        this.announceMapper = announceMapper;
    }

    /**
     *
     * the method returns all announces
     */
    @Override
    public List<AnnounceDtoOut> getAll() {
        return announceRepository.findAll().stream()
                .map(announceMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * the method returns all user announces
     */

    @Override
    public List<AnnounceDtoOut> getAllOfUser(String email) {
        var userPk = userRepository.findFirstByEmail(email).orElseThrow(NotFoundUserException::new).getId();
        return announceRepository.findAllById(userPk).stream()
                .map(announceMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * the method returns announce by id
     */

    @Override
    public AnnounceDtoIn get(Integer id) {
        var announce = announceRepository.findById(id)
                .orElseThrow(NotFoundAnnounceException::new);
        var announceDtoIn = new AnnounceDtoIn();

        fillUsersFields(announceDtoIn, Optional.ofNullable(announce.getAuthor()).orElseThrow(NotFoundUserException::new));

        announceDtoIn.setDescription(announce.getDescription());
        announceDtoIn.setImage(announce.getImage());
        announceDtoIn.setPrice(announce.getPrice());
        announceDtoIn.setTitle(announce.getTitle());
        return announceDtoIn;
    }

    /**
     *
     * the method add announce
     */
    @Override
    public AnnounceDtoOut add(CreateOrUpdateAd properties,
                              MultipartFile image,
                              String email) throws IOException {
        var announceDtoIn = new AnnounceDtoIn();

        fillUsersFields(announceDtoIn, userRepository.findFirstByEmail(email).orElseThrow(NotFoundUserException::new));

        announceDtoIn.setDescription(properties.getDescription());
        announceDtoIn.setImage(Arrays.toString(image.getBytes()));
        announceDtoIn.setPrice(properties.getPrice());
        announceDtoIn.setTitle(properties.getTitle());
        return announceMapper.toDTO(announceRepository.save(announceMapper.toEntity(announceDtoIn))); //TODO заменить null на путь к картинке и автора объявления
    }

    /**
     *
     * the method update announce info
     */

    @PreAuthorize("hasRole('ADMIN') or @announceServiceImpl.checkAuthor(principal, #announceId)")
    @Override
    public AnnounceDtoOut updateInfo(Integer id, CreateOrUpdateAd property) {
        Announce announce = announceRepository.findById(id)
                .orElseThrow(NotFoundAnnounceException::new);

        announce.setDescription(property.getDescription());
        announce.setTitle(announce.getTitle());
        announce.setPrice(property.getPrice());
        return announceMapper.toDTO(announceRepository.save(announce));
    }

    /**
     *
     * the method update announce image
     */
    @PreAuthorize("hasRole('ADMIN') or @announceServiceImpl.checkAuthor(principal, #announceId)")
    @Override
    public void updateImage(Integer id, MultipartFile image) throws IOException {
        Announce announce = announceRepository.findById(id)
                .orElseThrow(NotFoundAnnounceException::new);
        announce.setImage(Arrays.toString(image.getBytes()));
    }

    /**
     *
     * the method delete announce
     */
    @PreAuthorize("hasRole('ADMIN') or @announceServiceImpl.checkAuthor(principal, #announceId)")
    @Override
    public void delete(Integer id) {
        announceRepository.delete(
                announceRepository.findById(id).orElseThrow(RuntimeException::new)
        );
    }

    /**
     *
     * the method checks the user
     */
    private boolean checkAuthor(Principal principal, int announceId) {
        Announce announce = announceRepository.findById(announceId).orElseThrow();
        return announce.getAuthor().getEmail().equals(principal.getName());
    }

    /**
     *
     * the method fills some of the fields of the AnnounceDtoIn object
     */
    private void fillUsersFields(AnnounceDtoIn announceDtoIn, User user) {
        announceDtoIn.setPk(user.getId());
        announceDtoIn.setAuthorFirstName(user.getFirstName());
        announceDtoIn.setAuthorLastName(user.getLastName());
        announceDtoIn.setEmail(user.getEmail());
        announceDtoIn.setPhone(user.getPhone());
    }
}
