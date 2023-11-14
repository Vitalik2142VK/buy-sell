package ru.skypro.homework.service;

import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.announce.AnnounceDtoOut;
import ru.skypro.homework.dto.announce.CreateOrUpdateAd;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.mapping.AnnounceMapper;
import ru.skypro.homework.repository.AnnounceRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AnnounceServiceTest {

    @Spy
    private AnnounceService announceService;
    @Spy
    private AnnounceMapper announceMapper;
    @Mock
    private AnnounceRepository announceRepository;

    @Test
    public void updateInfo() throws JSONException {
        String olds = "old";
        String news = "new";
        var price = 1000;
        var num = 0;
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setPrice(price);
        createOrUpdateAd.setTitle(news);
        Announce start = new Announce();
        start.setTitle(olds);
        start.setPrice(num);
        Announce end = new Announce();
        end.setTitle(news);
        end.setPrice(price);
        Mockito.when(announceRepository.findById(num))
                .thenReturn(Optional.of(start));
        Mockito.when(announceRepository.save(start)).thenReturn(end);
                AnnounceDtoOut announceDtoOut = announceService.updateInfo(num, createOrUpdateAd);
        Assertions.assertThat(announceDtoOut.getTitle()).isEqualTo(olds);
        Assertions.assertThat(announceDtoOut.getPrice()).isEqualTo(price);
    }
}
