<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Secured page</title>
</head>
<body>
    <div>
        
        <h1>Hello, ${userDetails.username}!</h1>
        
        <sec:authorize access="isAuthenticated()">
            <p>This page is available only for authenticated users</p>
            <p><a href="${constants['cas.url']}/logout">Sign Out</a></p>
        </sec:authorize>

        <sec:authorize access="hasRole('admin')">
            <p>
                <a href="<c:url value="/admin"/> ">Admin page</a>
            </p>
        </sec:authorize>
    </div>
</body>
</html>