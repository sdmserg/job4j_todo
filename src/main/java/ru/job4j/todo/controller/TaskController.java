package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.model.Task;

import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/list")
    public String getAllTasks(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        model.addAttribute("tasks", taskService.findAll(user.getId()));
        return "tasks/list";
    }

    @GetMapping("/list/new")
    public String getNewTasks(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        model.addAttribute("tasks", taskService.findByDone(false, user.getId()));
        return "tasks/list";
    }

    @GetMapping("/list/completed")
    public String getCompletedTasks(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        model.addAttribute("tasks", taskService.findByDone(true, user.getId()));
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreateTaskPage() {
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, HttpSession session) {
        var user = (User) session.getAttribute("user");
        task.setUser(user);
        taskService.add(task);
        return "redirect:/tasks/list";
    }

    @GetMapping("/{id}")
    public String getDescriptionTask(Model model, @PathVariable int id) {
        model.addAttribute("task", taskService.findById(id));
        return "tasks/descriptionTask";
    }

    @PostMapping("/{id}/done")
    public String completeTask(@PathVariable int id) {
        taskService.completeTask(id);
        return "redirect:/tasks/" + id;
    }

    @GetMapping("/{id}/update")
    public String updateTask(Model model, @PathVariable int id) {
        model.addAttribute("task", taskService.findById(id));
        return "tasks/editTask";
    }

    @PostMapping("/{id}/update")
    public String updateTask(Model model, @ModelAttribute Task task,
                             @PathVariable int id) {
        taskService.update(task);
        return "redirect:/tasks/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deleteTask(Model model, @PathVariable int id) {
        taskService.deleteById(id);
        return "redirect:/tasks/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElement(NoSuchElementException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "errors/404";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "errors/500";
    }
}