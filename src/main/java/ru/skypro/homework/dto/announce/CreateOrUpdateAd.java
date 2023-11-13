package ru.skypro.homework.dto.announce;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateOrUpdateAd {
    private String title;
    private int price;
    private String description;
}
