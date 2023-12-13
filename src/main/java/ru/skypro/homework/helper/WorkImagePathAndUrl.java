package ru.skypro.homework.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.entity.User;

import java.io.IOException;

/**
 * Specifies the path to the image and the URL to get the image
 */
@Component
public class WorkImagePathAndUrl {
    @Value("${user.image}")
    private String userPath;
    @Value("${user.url}")
    private String userUrl;
    @Value("${announce.image}")
    private String announcePath;
    @Value("${announce.url}")
    private String announceUrl;

    /**
     * Get the path to the image for {@link User}
     * @return {@link String} -> <b>Format:<b> imageUser\nameImage
     */
    public String getUserImagePath(String image) throws IOException {
        return userPath + '\\' + image;
    }

    /**
     * Get the url to the image for {@link User}
     * @return {@link String} -> <b>format:<b> /users/imageUser/nameImage
     */
    public String getUserImageUrl(String image) {
        return '/' + userUrl + '/' + userPath + '/' + image;
    }

    /**
     * Get the path to the image for {@link Announce}
     * @return {@link String} -> <b>Format:<b> imageAnnounce\nameImage
     */
    public String getAdsImagePath(String image) throws IOException {
        return announcePath + '\\' + image;
    }

    /**
     * Get the url to the image for {@link Announce}
     * @return {@link String} -> <b>format:<b> /ads/imageAnnounce/nameImage
     */
    public String getAdsImageUrl(String image) {
        return '/' + announceUrl + '/' + announcePath + '/' + image;
    }
}
