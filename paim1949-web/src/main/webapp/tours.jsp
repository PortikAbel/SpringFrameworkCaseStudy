<%@ page import = "java.util.Collection" %>
<%@ page import = "edu.bbte.idde.paim1949.backend.model.Tour" %>
<%@ page import = "edu.bbte.idde.paim1949.backend.dao.TourDao" %>
<%@ page import = "edu.bbte.idde.paim1949.backend.dao.TourDaoFactory" %>

<% TourDao tourDao = TourDaoFactory.getTourMemDao(); %>

<html>
    <jsp:include page="partials/head.jsp">
        <jsp:param name="title" value="Tours" />
    </jsp:include>
    <body>
        <%@ include file="partials/nav.jsp" %>
        <% Collection<Tour> allTours = tourDao.findAll(); %>
        <% if (allTours.isEmpty()) { %>
            <p>No tours found.</p>
        <% } else { %>
            <table>
                <tr>
                    <th>distance in km</th>
                    <th>elevation in m</th>
                    <th>sign shape</th>
                    <th>sign colour</th>
                    <th>days recommended</th>
                </tr>
                <% for (Tour tour : allTours) { %>
                    <tr>
                        <td><%= tour.getDistanceInKm() %></td>
                        <td><%= tour.getElevationInM() %></td>
                        <td><%= tour.getSignShape() %></td>
                        <td><%= tour.getSignColour() %></td>
                        <td><%= tour.getDaysRecommended() %></td>
                    </tr>
                <% } %>
            <table>
        <% } %>
    </body>
</html>