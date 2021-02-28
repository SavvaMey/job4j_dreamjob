package servlet;

import model.User;
import store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        User user = PsqlStore.instOf().findByEmailUser(email);
        if (user == null) {
            req.setAttribute("error", "Пользователь с указанным email не существует");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
        String password = req.getParameter("password");
        if (!user.getPassword().equals(password)) {
            req.setAttribute("error", "Неправильный пароль");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
        HttpSession sc = req.getSession();
        sc.setAttribute("user", user);
        resp.sendRedirect(req.getContextPath() + "/posts.do");
    }
}
