package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User,Integer> {
}
