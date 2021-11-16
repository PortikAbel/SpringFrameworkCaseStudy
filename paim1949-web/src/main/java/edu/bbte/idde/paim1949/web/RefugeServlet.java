package edu.bbte.idde.paim1949.web;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.paim1949.backend.dao.AbstractDaoFactory;
import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.model.Refuge;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet("/refuges")
public class RefugeServlet extends HttpServlet {
    private RefugeDao refugeDao;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        refugeDao = AbstractDaoFactory.getDaoFactory().getRefugeDao();
        objectMapper = ObjectMapperFactory.getObjectMapper();
        log.info("Server initialized");
    }

    @Override
    public void destroy() {
        log.info("Destroying server");
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.setHeader("Content-Type", "application/json");
            objectMapper.writeValue(resp.getOutputStream(), refugeDao.findAll());
        } else {
            try {
                Long id = Long.parseLong(idParam);
                Refuge refuge = refugeDao.findById(id);

                if (refuge == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println("Refuge with id " + id + " not found");
                } else {
                    resp.setHeader("Content-Type", "application/json");
                    objectMapper.writeValue(resp.getOutputStream(), refuge);
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Invalid id");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Refuge refuge = objectMapper.readValue(req.getInputStream(), Refuge.class);
            Refuge createdRefuge = refugeDao.create(refuge);
            resp.setHeader("Content-Type", "application/json");
            objectMapper.writeValue(resp.getOutputStream(), createdRefuge);
        } catch (DatabindException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Invalid refuge:" + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        try {
            Long id = Long.parseLong(idParam);
            Refuge refuge = refugeDao.findById(id);

            if (refuge == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().println("Refuge with id " + id + " not found");
            } else {
                refuge = refugeDao.delete(id);
                objectMapper.writeValue(resp.getOutputStream(), refuge);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Invalid id");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        try {
            Long id = Long.parseLong(idParam);
            Refuge refuge = refugeDao.findById(id);

            if (refuge == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().println("Refuge with id " + id + " not found");
            } else {
                try {
                    refuge = objectMapper.readValue(req.getInputStream(), Refuge.class);
                    Refuge oldRefuge = refugeDao.update(id, refuge);
                    resp.setHeader("Content-Type", "application/json");
                    objectMapper.writeValue(resp.getOutputStream(), oldRefuge);
                } catch (DatabindException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().println("Invalid refuge:" + e.getMessage());
                }
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Invalid id");
        }
    }
}
