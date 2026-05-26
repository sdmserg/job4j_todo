package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskStore implements TaskStoreRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(TaskStore.class);
    private final SessionFactory sf;

    @Override
    public Task add(Task task) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(task);
                transaction.commit();
                return task;
            } catch (HibernateException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                LOGGER.error("Failed to add task: {}", task, e);
                throw new IllegalStateException("Database error while saving task", e);
            }
        }
    }

    @Override
    public boolean update(Task task) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int rowsUpdated = session.createQuery(
                                "UPDATE Task SET description = :fDescription, "
                                        + "done = :fDone, title = :fTitle WHERE id =:fId")
                        .setParameter("fDescription", task.getDescription())
                        .setParameter("fDone", task.isDone())
                        .setParameter("fTitle", task.getTitle())
                        .setParameter("fId", task.getId())
                        .executeUpdate();
                transaction.commit();
                return rowsUpdated > 0;
            } catch (HibernateException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                LOGGER.error("Failed to update task: {}", task, e);
                throw new IllegalStateException("Database error while updating task", e);
            }
        }
    }

    @Override
    public boolean completeTask(int id) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int rowsUpdate = session.createQuery(
                                "UPDATE Task SET done = true WHERE id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate();
                transaction.commit();
                return rowsUpdate > 0;
            } catch (HibernateException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                LOGGER.error("Failed to complete task with id: {}", id, e);
                throw new IllegalStateException("Database error while completing task", e);
            }
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int rowsDeleted = session.createQuery(
                                "DELETE FROM Task WHERE id =:fId")
                        .setParameter("fId", id)
                        .executeUpdate();
                transaction.commit();
                return rowsDeleted > 0;
            } catch (HibernateException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                LOGGER.error("Failed to delete task with id: {}", id, e);
                throw new IllegalStateException("Database error while deleting task", e);
            }
        }
    }

    @Override
    public Optional<Task> findById(int id) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                var task = session.createQuery(
                                "FROM Task WHERE id = :fId", Task.class)
                        .setParameter("fId", id)
                        .uniqueResultOptional();
                transaction.commit();
                return task;
            } catch (HibernateException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                LOGGER.error("Failed to find task with id: {}", id, e);
                throw new IllegalStateException("Database error while finding task", e);
            }
        }
    }

    @Override
    public Collection<Task> findByDone(boolean done) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                var task = session.createQuery(
                                "FROM Task WHERE done = :fDone", Task.class)
                        .setParameter("fDone", done)
                        .list();
                transaction.commit();
                return task;
            } catch (HibernateException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                LOGGER.error("Failed to find tasks", e);
                throw new IllegalStateException("Database error while finding tasks", e);
            }
        }
    }

    @Override
    public Collection<Task> findAll() {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                var task = session.createQuery(
                                "FROM Task", Task.class)
                        .list();
                transaction.commit();
                return task;
            } catch (HibernateException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                LOGGER.error("Failed to find tasks", e);
                throw new IllegalStateException("Database error while finding tasks", e);
            }
        }
    }
}