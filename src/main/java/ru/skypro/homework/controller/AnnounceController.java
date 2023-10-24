package ru.skypro.homework.controller;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.announce.AnnounceOut;
import ru.skypro.homework.dto.announce.CreateOrUpdateAd;
import ru.skypro.homework.service.AnnounceService;

import java.util.List;

@RestController
@RequestMapping("/ads")
public class AnnounceController {
    private final AnnounceService announceService;

    public AnnounceController(AnnounceService announceService) {
        this.announceService = announceService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<AnnounceOut> results = announceService.getAll();
        long count = results.size();
        return ResponseEntity.status(HttpStatus.OK).body(Pair.of(count, results));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> add(@RequestParam CreateOrUpdateAd properties,
                                 @RequestPart MultipartFile image) {
        return ResponseEntity.status(HttpStatus.CREATED).body(announceService.add(properties, image));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(announceService.get(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        announceService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> updateInfo(@PathVariable Long id,
                                    @RequestParam CreateOrUpdateAd property) {
        return ResponseEntity.status(HttpStatus.OK).body(announceService.updateInfo(id, property));
    }

    @GetMapping("me")
    public ResponseEntity<?> getUsersAll() {
        List<AnnounceOut> results = announceService.getUsersAll();
        long count = results.size();
        return ResponseEntity.status(HttpStatus.OK).body(Pair.of(count, results));
    }

    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable Long id,
                                         @RequestPart MultipartFile image) {
        announceService.updateImage(id, image);
        return ResponseEntity.status(HttpStatus.OK).body("string");
    }
}
