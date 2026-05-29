package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskStoreRepository {
    Task add(Task task);

    boolean update(Task task);

    boolean completeTask(int id);

    boolean deleteById(int id);

    Optional<Task> findById(int id);

    Collection<Task> findByDone(boolean done, int userid);

    Collection<Task> findAll(int userid);
}