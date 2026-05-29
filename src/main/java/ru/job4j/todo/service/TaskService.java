package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {

    Task add(Task task);

    void update(Task task);

    void completeTask(int id);

    void deleteById(int id);

    Task findById(int id);

    Collection<Task> findByDone(boolean done, int userId);

    Collection<Task> findAll(int userId);
}
