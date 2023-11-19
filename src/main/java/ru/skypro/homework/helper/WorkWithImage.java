package ru.skypro.homework.helper;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WorkWithImage {
    private WorkWithImage() {}

    /**
     * Save the new image.
     */
    public static String saveAndGetStringImage(String directory, String name, MultipartFile image) throws IOException {
        Path filePath = Path.of(directory, name + "." + getExtensions(image.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        Files.write(filePath, image.getBytes());
        return filePath.toFile().getName();
    }

    /**
     * Update the image.
     */
    public static String updateAndGetStringImage(String directory, String oldImage, MultipartFile image) throws IOException {
        String name = oldImage.substring(0, oldImage.lastIndexOf('.'));

        Path pathOld = Path.of(oldImage);
        Files.deleteIfExists(pathOld);

        return saveAndGetStringImage(directory, name, image);
    }

    /**
     * Load the image.
     */
    public static byte[] loadImage(String imagePath) throws IOException {
        return  Files.readAllBytes(Paths.get(imagePath));
    }

    /**
     * Load the image.
     */
    public static void removeImage(String imagePath) throws IOException {
        Path pathOld = Path.of(imagePath);
        Files.deleteIfExists(pathOld);
    }

    private static String getExtensions(String fileName) {
        if (fileName == null) {
            throw new NullPointerException();
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
