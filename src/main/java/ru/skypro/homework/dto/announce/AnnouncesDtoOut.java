package ru.skypro.homework.dto.announce;

import lombok.Data;

import java.util.List;

@Data
public class AnnouncesDtoOut {
    private int count;
    private List<AnnounceDtoOut> results;
}
