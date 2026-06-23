package ru.job4j.todo.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;

import org.hibernate.HibernateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

import ru.job4j.todo.model.Task;

@Repository
@AllArgsConstructor
public class TaskStore implements TaskStoreRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStore.class);
    private final CrudRepository crudRepository;

    @Override
    public Task add(Task task) {
        try {
            crudRepository.tx(session -> session.save(task));
            return task;
        } catch (HibernateException e) {
            LOGGER.error("Failed to add task: {}", task, e);
            throw new IllegalStateException("Database error while saving task", e);
        }
    }

    @Override
    public boolean update(Task task) {
        try {
            return crudRepository.run(
                    "UPDATE Task SET description = :fDescription, "
                            + "done = :fDone, title = :fTitle WHERE id =:fId",
                    Map.of("fDescription", task.getDescription(),
                            "fDone", task.isDone(),
                            "fTitle", task.getTitle(),
                            "fId", task.getId()
                    )
            );
        } catch (HibernateException e) {
                LOGGER.error("Failed to update task: {}", task, e);
                throw new IllegalStateException("Database error while updating task", e);
            }
        }

    @Override
    public boolean completeTask(int id) {
        try {
            return crudRepository.run(
                    "UPDATE Task SET done = true WHERE id = :fId",
                        Map.of("fId", id)
            );
        } catch (HibernateException e) {
                LOGGER.error("Failed to complete task with id: {}", id, e);
                throw new IllegalStateException("Database error while completing task", e);
            }
        }

    @Override
    public boolean deleteById(int id) {
        try {
            return crudRepository.run(
                    "DELETE FROM Task WHERE id =:fId",
                    Map.of("fId", id)
            );
        } catch (HibernateException e) {
            LOGGER.error("Failed to delete task with id: {}", id, e);
            throw new IllegalStateException("Database error while deleting task", e);
        }
    }

    @Override
    public Optional<Task> findById(int id) {
        try {
            return crudRepository.optional(
                    "FROM Task f JOIN FETCH f.priority WHERE id = :fId",
                    Task.class,
                    Map.of("fId", id)
            );
        } catch (HibernateException e) {
            LOGGER.error("Failed to find task with id: {}", id, e);
            throw new IllegalStateException("Database error while finding task", e);
        }
    }

    @Override
    public Collection<Task> findByDone(boolean done, int userId) {
        try {
            return crudRepository.query(
                    "FROM Task f JOIN FETCH f.priority WHERE done = :fDone AND user_id = :fId",
                          Task.class,
                          Map.of("fDone", done,
                                  "fId", userId)
            );
        } catch (HibernateException e) {
            LOGGER.error("Failed to find tasks", e);
            throw new IllegalStateException("Database error while finding tasks", e);
        }
    }

    @Override
    public Collection<Task> findAll(int userId) {
        try {
            return crudRepository.query(
                    "FROM Task f JOIN FETCH f.priority WHERE user_id = :fId ",
                    Task.class,
                    Map.of("fId", userId)
            );
        } catch (HibernateException e) {
            LOGGER.error("Failed to find tasks", e);
            throw new IllegalStateException("Database error while finding tasks", e);
        }
    }
}