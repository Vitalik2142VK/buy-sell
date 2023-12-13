package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.UserAuth;
import ru.skypro.homework.dto.announce.AnnouncesDtoOut;
import ru.skypro.homework.dto.announce.CreateOrUpdateAd;
import ru.skypro.homework.dto.announce.AnnounceDtoIn;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundAnnounceException;
import ru.skypro.homework.exception.NotFoundUserException;
import ru.skypro.homework.exception.UserNotAuthorAnnounceException;
import ru.skypro.homework.helper.WorkImagePathAndUrl;
import ru.skypro.homework.helper.WorkWithImage;
import ru.skypro.homework.mapping.AnnounceMapper;
import ru.skypro.homework.repository.AnnounceRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AnnounceService;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Service
public class AnnounceServiceImpl implements AnnounceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnounceServiceImpl.class);
    private final AnnounceRepository announceRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AnnounceMapper announceMapper;
    private final WorkImagePathAndUrl getPathImage;

    @Value("${announce.image}")
    private String imagePath;

    public AnnounceServiceImpl(AnnounceRepository announceRepository,
                               UserRepository userRepository,
                               CommentRepository commentRepository,
                               AnnounceMapper announceMapper,
                               WorkImagePathAndUrl getPathImage) {
        this.announceRepository = announceRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.announceMapper = announceMapper;
        this.getPathImage = getPathImage;
    }

    /**
     * The method returns all announces.
     */
    @Override
    public AnnouncesDtoOut getAll() {
        return announceMapper.announceListToAnnounceDtoOutList(announceRepository.findAll());
    }

    /**
     * The method returns all user announces.
     */
    @Override
    public AnnouncesDtoOut getAllOfUser(UserAuth userDetails) {
        var userPk = userDetails.getUser().orElseThrow(NotFoundUserException::new).getId();
        return announceMapper.announceListToAnnounceDtoOutList(announceRepository.findAllByAuthor_Id(userPk));
    }

    /**
     * The method add announce.
     */
    @Override
    public AnnounceDtoOut add(CreateOrUpdateAd properties,
                              MultipartFile image,
                              UserAuth userDetails) throws IOException {
        User user = userDetails.getUser().orElseThrow(NotFoundUserException::new);
        int numberAds = announceRepository.getNumberUserAds(user.getId()) + 1;
        String fileName = "Ads_" + numberAds + "_auth_" + user.getId() + "_lg_" + user.getEmail().hashCode();
        Announce announce = announceRepository.save(announceMapper.toAnnounce(properties, user, WorkWithImage.saveAndGetStringImage(imagePath, fileName, image)));
        LOGGER.debug("Add new " + announce.getAuthor() + ": " + announce.getTitle() + " " + announce.getDescription());
        return announceMapper.toDTO(announce);
    }

    /**
     * The method returns announce by id.
     */
    @Override
    public AnnounceDtoIn get(Integer id) {
        var announce = announceRepository.findById(id).orElseThrow(NotFoundAnnounceException::new);
        return announceMapper.toDtoIn(announce, Optional.ofNullable(announce.getAuthor()).orElseThrow(NotFoundUserException::new));
    }

    /**
     * The method delete announce.
     */
    @Override
    @PreAuthorize("hasRole('ADMIN') or @announceServiceImpl.checkAuthor(principal, #announceId)")
    public void delete(Integer announceId) throws IOException {
        var announce = announceRepository.findById(announceId).orElseThrow(NotFoundAnnounceException::new);
        var comments = commentRepository.findAllByAd_IdOrderByCreatedAtDesc(announceId);
        if (comments != null && !(comments.isEmpty())) {
            comments.forEach(commentRepository::delete);
        }
        WorkWithImage.removeImage(getPathImage.getAdsImagePath(announce.getImage()));
        LOGGER.debug("Successfully deleted announce - " + announce);
        announceRepository.delete(announce);
    }

    /**
     * The method update announce info.
     */
    @Override
    @PreAuthorize("hasRole('ADMIN') or @announceServiceImpl.checkAuthor(principal, #announceId)")
    public AnnounceDtoOut updateInfo(Integer announceId, CreateOrUpdateAd property) {
        var announce = announceRepository.findById(announceId).orElseThrow(NotFoundAnnounceException::new);
        return announceMapper.toDTO(announceRepository.save(announceMapper.toAnnounce(property, announce)));
    }

    /**
     * The method update announce image.
     */
    @Override
    @PreAuthorize("hasRole('ADMIN') or @announceServiceImpl.checkAuthor(principal, #announceId)")
    public String updateImage(Integer announceId, MultipartFile image) throws IOException {
        var announce = announceRepository.findById(announceId).orElseThrow(NotFoundAnnounceException::new);

        announce.setImage(WorkWithImage.updateAndGetStringImage(imagePath, announce.getImage(), image));
        announceRepository.save(announce);
        return announce.getImage();
    }

    /**
     * Returns an array of image bytes
     */
    @Override
    public byte[] getImage(String nameImage) throws IOException {
        return WorkWithImage.loadImage(getPathImage.getAdsImagePath(nameImage));
    }

    /**
     * The method checks the user.
     */
    private boolean checkAuthor(Principal principal, int announceId) {
        int idUser = userRepository.getIdUserByEmail(principal.getName()).orElseThrow(NotFoundUserException::new);
        return announceRepository.checkAuthorAnnounce(announceId, idUser).orElseThrow(UserNotAuthorAnnounceException::new);
    }
}
