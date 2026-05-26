package ru.job4j.todo.exception;

public class DuplicateLoginException extends RuntimeException {
    private final String login;

    public DuplicateLoginException(String login, Throwable cause) {
        super("Login '" + login + "' already exists", cause);
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}