package ru.skypro.homework.dto.announce;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class AnnounceDtoOut {

    @Schema(description = "id автора объявления")
    private int author;

    @Schema(description = "ссылка на картинку объявления")
    private String image;

    @Schema(description = "id объявления")
    private int pk;

    @Schema(description = "цена объявления")
    private int price;

    @Schema(description = "заголовок объявления")
    private String title;

}
