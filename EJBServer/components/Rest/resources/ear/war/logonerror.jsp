<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Unsuccessful Login</title>
  </head>

<body dir="ltr">
<h2 class="login-welcome">Unsuccessful Login</h2>

  <form action="j_security_check" id="loginform" method="post" name="loginform">
    <label for="j_username">User name:</label>
    </br>
    <input title="Enter Your Username" name="j_username" type="text" id="j_username" tabindex="1">
    </br>
    <label for="j_password">Password:</label>
    </br>
    <input title="Enter Your Password" name="j_password" type="password" id="j_password" tabindex="2" autocomplete="off">
    <input title="Click here to log in" value="Login" name="action" type="submit" class="submit">
    </br>
    </br>
    <label for="user_type">External User:</label>
    <input type="checkbox" name="user_type" value="EXTERNAL" />
    </br>
  </form>
<%
    // Set the response type to be 401 unauthorized.
    response.setStatus(javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);    
%>

<%@ page session="false"%>

</body>
</html>
