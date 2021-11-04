package edu.bbte.idde.paim1949.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            LOG.info("login successful");
            req.getSession().setAttribute("username", username);
            req.getSession().removeAttribute("error");
            resp.sendRedirect("index");
        } else {
            LOG.info("login unsuccessful");
            req.getSession().setAttribute("error", true);
            resp.sendRedirect("login");
        }
    }
}
