package edu.bbte.idde.paim1949.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter({"/", "/index"})
public class IndexFilter extends HttpFilter {
    private static final Logger LOG = LoggerFactory.getLogger(IndexFilter.class);

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        if (req.getRequestURI().startsWith("/static")) {
            chain.doFilter(req, res);
        }
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            LOG.info("Redirecting to login page");
            res.sendRedirect("login");
        } else {
            chain.doFilter(req, res);
        }
    }
}
