package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skypro.homework.entity.Announce;

import java.util.List;
import java.util.Optional;

public interface AnnounceRepository extends JpaRepository<Announce, Integer> {
    List<Announce> findAllById(Integer id);

    @Query(value = "SELECT true FROM Announce a WHERE a.id = :idAds AND a.author.id = :idAuthor")
    Optional<Boolean> checkAuthorAnnounce(@Param("idAds") Integer idAds, @Param("idAuthor") Integer idAuthor);

    @Query(value = "SELECT true FROM Announce a WHERE a.id = :idAds")
    Optional<Boolean> checkExistAnnounce(@Param("idAds") Integer idAds);
}
