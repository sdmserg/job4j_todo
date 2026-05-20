package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {

    Task add(Task task);

    boolean update(Task task);

    boolean completeTask(int id);

    boolean deleteById(int id);

    Task findById(int id);

    Collection<Task> findByDone(boolean done);

    Collection<Task> findAll();
}
