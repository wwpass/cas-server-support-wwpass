
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Reqister</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>

    <div>
        <form:form  action="./new" method="post" modelAttribute="registerForm" id="register-form">
            
            <h1>Register form:</h1>
            <form:errors path="*" element="div" cssStyle="color: red" />

            <c:if test="${param.wwpassError != null}">
                <div style="color: red">WWPass authentication error: ${param.wwpassError}</div>
            </c:if>
            
            <div>
                <form:input path="username" id="username" placeholder="Username"/>
            </div>
            
            <div>
                <form:password path="password" id="password" placeholder="Password" />
            </div>
            
            <div>           
                <form:checkbox path="adminRole" id="adminRole" label="Do you want to add 'admin' authority to this user?"/>
            </div>
            
            
            <form:hidden path="ticket" id="ticket"/>

            
            
        </form:form>
        <button onClick="javascript:OnAuth();">Register and bind WWPass Keyset</button>
    </div>

</body>

<!-- wwpass auth -->
<script src="//cdn.wwpass.com/packages/latest/wwpass.js"></script>
<script type="text/javascript" charset="utf-8">

    function OnAuth() {
        wwpass_auth("${constants['wwpass.sp.name']}", function (status, response) {
            if (status == WWPass_OK) { //If ticket request handled successfully
                document.getElementById("ticket").setAttribute("value", response);
                var f = document.getElementById('register-form');
                f.submit();
            } else if (status == 603) { return false; }
            else {
                document.location = "?wwpassError=" + response; //If ticket request not handled, return error
            }
        });
        return false;
    }
</script>

</html>