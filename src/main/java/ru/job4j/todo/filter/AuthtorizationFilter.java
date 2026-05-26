package ru.job4j.todo.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class AuthtorizationFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain)
            throws IOException, ServletException {
        String uri = request.getRequestURI();
        if (isPublic(uri)) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = request.getSession(false);
        boolean isLogged = session != null && session.getAttribute("user") != null;
        if (!isLogged) {
            response.sendRedirect(request.getContextPath() + "/users/login");
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isPublic(String uri) {
        return uri.startsWith("/users/login")
                || uri.startsWith("/users/register")
                || uri.startsWith("/users/logout")
                || uri.startsWith("/css")
                || uri.startsWith("/js");
    }
}