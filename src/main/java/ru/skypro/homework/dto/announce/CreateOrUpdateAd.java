package ru.skypro.homework.dto.announce;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrUpdateAd {
    private String title;
    private int price;
    private String description;
}
