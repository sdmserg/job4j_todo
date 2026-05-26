package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.exception.DuplicateLoginException;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserStore implements UserStoreRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserStore.class);
    private final SessionFactory sf;

    @Override
    public User save(User user) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(user);
                transaction.commit();
                return user;
            } catch (ConstraintViolationException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                LOGGER.error("User registration failed. Login {} already exists!", user.getLogin(), e);
                throw new DuplicateLoginException(user.getLogin(), e);
            } catch (HibernateException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                LOGGER.error("Database error while registering user with login.html", user.getLogin(), e);
                throw new IllegalStateException(
                        "Database error during user registration", e
                );
            }
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                var user = session.createQuery("FROM User WHERE login = :fLogin", User.class)
                        .setParameter("fLogin", login)
                        .uniqueResultOptional();
                transaction.commit();
                return user;
            } catch (HibernateException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                LOGGER.error("Database error while finding user by login {}", login, e);
                throw new IllegalStateException("Database error while finding user", e);
            }
        }
    }
}