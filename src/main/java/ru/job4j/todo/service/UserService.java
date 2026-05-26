package ru.job4j.todo.service;

import ru.job4j.todo.model.User;

public interface UserService {

    User save(User user);

    User findByLoginAndPassword(String login, String password);
}