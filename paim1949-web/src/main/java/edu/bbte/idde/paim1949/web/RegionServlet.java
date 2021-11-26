package edu.bbte.idde.paim1949.web;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.paim1949.backend.dao.AbstractDaoFactory;
import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.model.Region;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet("/regions")
public class RegionServlet extends HttpServlet {
    private RegionDao regionDao;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        regionDao = AbstractDaoFactory.getDaoFactory().getRegionDao();
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
            objectMapper.writeValue(resp.getOutputStream(), regionDao.findAll());
        } else {
            try {
                Long id = Long.parseLong(idParam);
                Region region = regionDao.findById(id);

                if (region == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println("Region with id " + id + " not found");
                } else {
                    resp.setHeader("Content-Type", "application/json");
                    objectMapper.writeValue(resp.getOutputStream(), region);
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
            Region region = objectMapper.readValue(req.getInputStream(), Region.class);
            Region createdRegion = regionDao.create(region);
            resp.setHeader("Content-Type", "application/json");
            objectMapper.writeValue(resp.getOutputStream(), createdRegion);
        } catch (DatabindException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Invalid region:" + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        try {
            Long id = Long.parseLong(idParam);
            Region region = regionDao.findById(id);

            if (region == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().println("Region with id " + id + " not found");
            } else {
                region = regionDao.delete(id);
                objectMapper.writeValue(resp.getOutputStream(), region);
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
            Region region = regionDao.findById(id);

            if (region == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().println("Region with id " + id + " not found");
            } else {
                try {
                    region = objectMapper.readValue(req.getInputStream(), Region.class);
                    Region oldRegion = regionDao.update(id, region);
                    resp.setHeader("Content-Type", "application/json");
                    objectMapper.writeValue(resp.getOutputStream(), oldRegion);
                } catch (DatabindException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().println("Invalid region:" + e.getMessage());
                }
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Invalid id");
        }
    }
}
