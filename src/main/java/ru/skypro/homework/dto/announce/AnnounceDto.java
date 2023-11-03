package ru.skypro.homework.dto.announce;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnounceDto {
    private int pk;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private int price;
    private String title;
}
