<html>
    <jsp:include page="partials/head.jsp">
        <jsp:param name="title" value="Login" />
    </jsp:include>
    <body>
        <form action="login" method="post">
            <% if (request.getSession().getAttribute("error") != null) { %>
                <p>An error occurred while logging in</p>
            <% } %>
            <table>
                <tr>
                    <td>User name:</td>
                    <td><input type="text" name="username" required/></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type="password" name="password" required/></td>
                </tr>
                <tr>
                    <td align="center"><input type="submit" value="Login"/></td>
                </tr>
            </table>
        </form>
    </body>
</html>