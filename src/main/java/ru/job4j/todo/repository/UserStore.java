package ru.job4j.todo.repository;

import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ru.job4j.todo.exception.DuplicateLoginException;
import ru.job4j.todo.model.User;

@Repository
@AllArgsConstructor
public class UserStore implements UserStoreRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserStore.class);
    private final CrudRepository crudRepository;

    @Override
    public User save(User user) {
        try {
            crudRepository.tx(session -> session.save(user));
            return user;
        } catch (ConstraintViolationException e) {
            LOGGER.error("User registration failed. Login {} already exists!", user.getLogin(), e);
            throw new DuplicateLoginException(user.getLogin(), e);
        } catch (HibernateException e) {
            LOGGER.error("Database error while registering user with login.html", user.getLogin(), e);
            throw new IllegalStateException(
                    "Database error during user registration", e
            );
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try {
            return crudRepository.optional(
                    "FROM User WHERE login = :fLogin",
                    User.class,
                    Map.of("fLogin", login)
            );
        } catch (HibernateException e) {
            LOGGER.error("Database error while finding user by login {}", login, e);
            throw new IllegalStateException("Database error while finding user", e);
        }
    }
}