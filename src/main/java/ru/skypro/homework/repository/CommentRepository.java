package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {



    List<Comment> findAllByAd_IdOrderByCreatedAtDesc(Integer id);

//    @Query(value = "SELECT c.author FROM Comment c WHERE c.id = :idComment")
//    Optional<User> findAuthorComment(@Param("idComment")Integer idComment);

    @Query(value = "SELECT (SELECT true FROM c.author u WHERE u.id = :idAuthor) FROM Comment c WHERE c.id = :idComment")
    boolean checkAuthorComment(@Param("idComment") Integer idComment, @Param("idAuthor") Integer idAuthor);
}
