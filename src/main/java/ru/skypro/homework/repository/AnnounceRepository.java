package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AnnounceEntity;

public interface AnnounceRepository extends JpaRepository<AnnounceEntity, Integer> {
}
