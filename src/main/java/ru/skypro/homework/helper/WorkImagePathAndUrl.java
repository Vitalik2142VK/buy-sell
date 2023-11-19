package ru.skypro.homework.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
     * Update the image.
     */
    public String getUserImagePath(String image) throws IOException {
        return userPath + '\\' + image;
    }

    /**
     * Update the image.
     */
    public String getUserImageUrl(String image) {
        return '/' + userUrl + '/' + userPath + '/' + image;
    }

    public String getAdsImagePath(String image) throws IOException {
        return announcePath + '\\' + image;
    }

    /**
     * Update the image.
     */
    public String getAdsImageUrl(String image) {
        return '/' + announceUrl + '/' + announcePath + '/' + image;
    }
}
