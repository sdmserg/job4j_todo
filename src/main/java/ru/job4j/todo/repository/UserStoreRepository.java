package ru.job4j.todo.repository;

import ru.job4j.todo.model.User;

import java.util.Optional;

public interface UserStoreRepository {
    User save(User user);

    Optional<User> findByLogin(String login);
}