<%@ page import="bean.User" %>
<%@ page import="database.crud.UserCrud" %>
<%@ page import="deprecateddbconnect.DatabaseConnectManager" %><%--
  Created by IntelliJ IDEA.
  User: will
  Date: 2019/2/25
  Time: 15:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  $END$
  </body>
</html>

<%
  User user = new User();
  user.setAccount("testDriud");
  user.setPassword("123456");
  user.setNickName("Awille");
  if (UserCrud.addUser(user) != null) {
    System.out.println("Success");
  } else {
    System.out.println("Fail");
  }
%>
