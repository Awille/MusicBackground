<%@ page import="threadpoolservice.ThreadPoolService" %>
<%@ page import="java.util.concurrent.FutureTask" %>
<%@ page import="utils.UserTask" %>
<%@ page import="utils.RequestProcessUtils" %><%--
  Created by IntelliJ IDEA.
  User: will
  Date: 2019/3/6
  Time: 12:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%
    System.out.println("user method");
    RequestProcessUtils.processUserRequest(request, out, application);
%>
