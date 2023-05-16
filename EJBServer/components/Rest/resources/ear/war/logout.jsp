<!DOCTYPE html>
<html>
  <head>
    <meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/>
    <title>Logout Page</title>
  </head>
  <body>
    <h2>Log out</h2>
    <p>Are you sure you want to log out?</p>
    <form id="logout" method="post" name="logout" action="logon.jsp">
      <input title="Click here to log out" value="Log Out" name="action" type="submit" class="submit" />
    </form>
<%
	request.logout();
    request.getSession().invalidate();
%>

<%@ page session="false"%>

</div></body></html>

