package servlet;

import model.User;
import store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("reg.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        if (PsqlStore.instOf().findByEmailUser(email) != null) {
            req.setAttribute("error", "Пользователь с указанным email уже зарегистрирован");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        }
        String username = req.getParameter("name");
        String password = req.getParameter("password");
        PsqlStore.instOf().createUser(new User(0, username, email, password));
        resp.sendRedirect("login.jsp");
    }
}
