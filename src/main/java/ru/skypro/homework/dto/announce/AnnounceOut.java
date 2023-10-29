package ru.skypro.homework.dto.announce;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnnounceOut {
    private int author;
    private String image;
    private int pk;
    private int price;
    private String title;
}
