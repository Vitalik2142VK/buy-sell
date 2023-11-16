package ru.skypro.homework.dto.announce;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnnounceDtoOut {
    private int author;
    private String image;
    private int pk;
    private int price;
    private String title;
}
