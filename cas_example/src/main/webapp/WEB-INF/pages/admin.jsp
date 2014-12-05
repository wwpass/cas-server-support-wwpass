<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
    </head>
    <body>
        <h1>Admin</h1>
        <p>Hello, ${userDetails.username}!</p>
        <p>This page available only for users with role "admin".</p>
        <p>Authorities for this user: ${userAuthorities}.</p>

        <a href="<c:url value='/secured'/>">Back</a>
    </body>
</html>