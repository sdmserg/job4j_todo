package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.model.Task;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/list")
    public String getAllTasks(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/list/new")
    public String getNewTasks(Model model) {
        model.addAttribute("tasks", taskService.findByDone(false));
        return "tasks/list";
    }

    @GetMapping("/list/completed")
    public String getCompletedTasks(Model model) {
        model.addAttribute("tasks", taskService.findByDone(true));
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreateTaskPage() {
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, Model model) {
        try {
            taskService.add(task);
            return "redirect:/tasks/list";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/500";
        }
    }

    @GetMapping("/{id}")
    public String getDescriptionTask(Model model, @PathVariable int id) {
        try {
            var taskOptional = taskService.findById(id);
            if (taskOptional.isEmpty()) {
                model.addAttribute("message", "Task not found!");
                return "errors/404";
            }
            model.addAttribute("task", taskOptional.get());
            return "tasks/descriptionTask";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/500";
        }
    }

    @PostMapping("/{id}/done")
    public String completeTask(Model model, @PathVariable int id) {
        try {
            taskService.completeTask(id);
            return "redirect:/tasks/" + id;
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/500";
        }
    }

    @GetMapping("/{id}/update")
    public String updateTask(Model model, @PathVariable int id) {
        try {
            var taskOptional = taskService.findById(id);
            if (taskOptional.isEmpty()) {
                model.addAttribute("message", "Task not found!");
                return "errors/404";
            }
            model.addAttribute("task", taskOptional.get());
            return "tasks/editTask";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/500";
        }
    }

    @PostMapping("/{id}/update")
    public String updateTask(Model model, @ModelAttribute Task task,
                             @PathVariable int id) {
        try {
            taskService.update(task);
            return "redirect:/tasks/" + id;
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/500";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteTask(Model model, @PathVariable int id) {
        try {
            taskService.deleteById(id);
            return "redirect:/tasks/list";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/500";
        }
    }
}