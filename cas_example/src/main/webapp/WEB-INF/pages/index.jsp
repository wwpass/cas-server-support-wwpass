<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<!doctype>
<html>
    <head>
        <title>Welcome</title>
    </head>
    <body>
        <div>
            <h1>Welcome</h1>
            <sec:authorize access="isAuthenticated()">
                <p>You are already authenticated!</p>
                <p><a href="${constants['cas.url']}/logout">Sign Out</a></p>
            </sec:authorize>
            <p>
                <a href="<c:url value='/secured'/>" >Go to secured page</a>
            </p> 
            <p>
                <a href="<c:url value="/register/form"/>">Register</a>
            </p>
        </div>
    </body>
</html>