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
public class TaskStory implements TaskStoryRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(TaskStory.class);
    private final SessionFactory sf;

    @Override
    public Task add(Task task) {
        Transaction transaction = null;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
            session.save(task);
            transaction.commit();
            return task;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to add task: {}", task, e);
            throw new IllegalStateException("Failed to add task", e);
        }
    }

    @Override
    public boolean update(Task task) {
        Transaction transaction = null;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
            int rowsUpdated = session.createQuery(
                    "UPDATE Task SET description = :fDescription, "
                    + "done = :fDone WHERE id =:fId")
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fDone", task.getDone())
                    .setParameter("fId", task.getId())
                    .executeUpdate();
            transaction.commit();
            return rowsUpdated > 0;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to update task: {}", task, e);
            throw new IllegalStateException("Failed to update task", e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        Transaction transaction = null;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
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
            throw new IllegalStateException("Failed to delete task", e);
        }
    }

    @Override
    public Optional<Task> findById(int id) {
        Transaction transaction = null;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
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
            throw new IllegalStateException("Failed to find task", e);
        }
    }

    @Override
    public Collection<Task> findByDone(boolean done) {
        Transaction transaction = null;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
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
            throw new IllegalStateException("Failed to find task", e);
        }
    }

    @Override
    public Collection<Task> findAll() {
        Transaction transaction = null;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
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
            throw new IllegalStateException("Failed to find task", e);
        }
    }
}