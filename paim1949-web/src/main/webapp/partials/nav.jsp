<% String username = (String)request.getSession(false).getAttribute("username"); %>

<nav>
    <span>Hello&nbsp;<%= username %>!</span>
    <a href="logout">Logout</a>
</nav>