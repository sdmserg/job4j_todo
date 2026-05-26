package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.exception.InvalidCredentialsException;
import ru.job4j.todo.exception.UserAlreadyExistsException;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String login,
                        @RequestParam String password,
                        HttpServletRequest request) {
        var findUser = userService.findByLoginAndPassword(login, password);
        HttpSession session = request.getSession();
        session.setAttribute("user", findUser);
        return "redirect:/tasks/list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExists(UserAlreadyExistsException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "users/register";
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public String handleInvalidCredentials(InvalidCredentialsException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "users/login";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleInvalidCredentials(IllegalStateException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "errors/500";
    }
}