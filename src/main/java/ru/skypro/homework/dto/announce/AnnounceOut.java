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
    private Long author;
    private String image;
    private Long pk;
    private Long price;
    private String title;
}
