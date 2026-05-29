package ru.job4j.todo.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskStoreRepository;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskStoreRepository taskStore;

    @Override
    public Task add(Task task) {
        return taskStore.add(task);
    }

    @Override
    public void update(Task task) {
        var isUpdated = taskStore.update(task);
        if (!isUpdated) {
            throw new NoSuchElementException(
                    "Task with id " + task.getId() + " not found!"
            );
        }
    }

    @Override
    public void completeTask(int id) {
        var isCompleted = taskStore.completeTask(id);
        if (!isCompleted) {
            throw new NoSuchElementException(
                    "Task with id " + id + " not found!"
            );
        }
    }

    @Override
    public void deleteById(int id) {
        var isDeleted = taskStore.deleteById(id);
        if (!isDeleted) {
            throw new NoSuchElementException(
                    "Task with id " + id + " not found!"
            );
        }
    }

    @Override
    public Task findById(int id) {
        return taskStore.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Task with id " + id + " not found!"
                        )
                );
    }

    @Override
    public Collection<Task> findByDone(boolean done, int userId) {
        return taskStore.findByDone(done, userId);
    }

    @Override
    public Collection<Task> findAll(int userId) {
        return taskStore.findAll(userId);
    }
}