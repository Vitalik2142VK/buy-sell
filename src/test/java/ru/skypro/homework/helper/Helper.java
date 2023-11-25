package ru.skypro.homework.helper;

import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.AnnounceRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

public class Helper {
    private Helper() {}

    public static User createUser(String email, String password, String firstName, String lastName, String phone, String image, Role role, PasswordEncoder encoder) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setImage(image);
        user.setRole(role);
        return user;
    }

    public static void insertUsers(UserRepository repository, PasswordEncoder encoder) {
        repository.save(createUser(
                "ivanov@gmail.com",
                "12345678",
                "Иван",
                "Иванов",
                "+78001111111",
                null,
                Role.USER,
                encoder
        ));
        repository.save(createUser(
                "petrov@gmail.com",
                "87654321",
                "Петр",
                "Петров",
                "+78002222222",
                null,
                Role.USER,
                encoder
        ));
    }

    public static Announce createAnnounce(User author, String description, String image, int price, String title) {
        Announce announce = new Announce();
        announce.setAuthor(author);
        announce.setDescription(description);
        announce.setImage(image);
        announce.setPrice(price);
        announce.setTitle(title);
        return announce;
    }

    public static Announce insertAnnounce(AnnounceRepository repository, User user) {
        Announce announce = createAnnounce(
                user,
                "Описание объявления",
                "null",
                1000,
                "Заголовок объявления"
        );
        repository.save(announce);
        return announce;
    }

    public static Comment createComment(User author, Announce ad, String text) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setAd(ad);
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setText(text);
        return comment;
    }

    public static void insertComment(CommentRepository repository, Announce ad, User user) {
        repository.save(createComment(
           user,
           ad,
           "Текст комментария"
        ));
    }
}
