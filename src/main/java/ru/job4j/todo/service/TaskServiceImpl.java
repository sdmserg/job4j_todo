package ru.job4j.todo.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskStoryRepository;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskStoryRepository taskStory;

    @Override
    public Task add(Task task) {
        return taskStory.add(task);
    }

    @Override
    public boolean update(Task task) {
        return taskStory.update(task);
    }

    @Override
    public boolean completeTask(int id) {
        return taskStory.completeTask(id);
    }

    @Override
    public boolean deleteById(int id) {
        return taskStory.deleteById(id);
    }

    @Override
    public Task findById(int id) {
        return taskStory.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Task not found!"));
    }

    @Override
    public Collection<Task> findByDone(boolean done) {
        return taskStory.findByDone(done);
    }

    @Override
    public Collection<Task> findAll() {
        return taskStory.findAll();
    }
}