package edu.bbte.idde.paim1949.web;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.dao.TourDaoFactory;
import edu.bbte.idde.paim1949.backend.model.Tour;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/tours")
public class TourServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(TourServlet.class);
    private TourDao tourDao;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        tourDao = TourDaoFactory.getTourMemDao();
        objectMapper = ObjectMapperFactory.getObjectMapper();
        LOG.info("Server initialized");
    }

    @Override
    public void destroy() {
        LOG.info("Destroying server");
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.setHeader("Content-Type", "application/json");
            objectMapper.writeValue(resp.getOutputStream(), tourDao.findAll());
        } else {
            try {
                Long id = Long.parseLong(idParam);
                Tour tour = tourDao.findById(id);

                if (tour == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println("Tour with id " + id + " not found");
                } else {
                    resp.setHeader("Content-Type", "application/json");
                    objectMapper.writeValue(resp.getOutputStream(), tour);
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Invalid id");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Tour tour = objectMapper.readValue(req.getInputStream(), Tour.class);
            Tour createdTour = tourDao.create(tour);
            resp.setHeader("Content-Type", "application/json");
            objectMapper.writeValue(resp.getOutputStream(), createdTour);
        } catch (DatabindException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Invalid tour:" + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        try {
            Long id = Long.parseLong(idParam);
            Tour tour = tourDao.findById(id);

            if (tour == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().println("Tour with id " + id + " not found");
            } else {
                tour = tourDao.delete(id);
                objectMapper.writeValue(resp.getOutputStream(), tour);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Invalid id");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        try {
            Long id = Long.parseLong(idParam);
            Tour tour = tourDao.findById(id);

            if (tour == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().println("Tour with id " + id + " not found");
            } else {
                try {
                    tour = objectMapper.readValue(req.getInputStream(), Tour.class);
                    Tour oldTour = tourDao.update(id, tour);
                    resp.setHeader("Content-Type", "application/json");
                    objectMapper.writeValue(resp.getOutputStream(), oldTour);
                } catch (DatabindException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().println("Invalid tour:" + e.getMessage());
                }
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Invalid id");
        }
    }
}
