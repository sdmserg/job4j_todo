package ru.job4j.todo.exception;

import javax.persistence.criteria.CriteriaBuilder;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}