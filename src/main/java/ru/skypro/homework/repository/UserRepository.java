package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skypro.homework.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    Optional<User> findFirstByEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Optional<Integer> getIdUserByEmail(@Param("email")String email);
}
