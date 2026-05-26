package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.exception.DuplicateLoginException;
import ru.job4j.todo.exception.InvalidCredentialsException;
import ru.job4j.todo.exception.UserAlreadyExistsException;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.UserStoreRepository;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStoreRepository userStore;

    @Override
    public User save(User user) {
        try {
            return userStore.save(user);
        } catch (DuplicateLoginException e) {
            throw new UserAlreadyExistsException(
                    "User with login " + e.getLogin() + " already exists!"
            );
        }
    }

    @Override
    public User findByLoginAndPassword(String login, String password) {
        return userStore.findByLogin(login)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(
                        () -> new InvalidCredentialsException(
                                "Invalid login or password"
                        )
                );
    }
}