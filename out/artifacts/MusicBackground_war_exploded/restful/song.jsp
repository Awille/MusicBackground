<%@ page import="utils.RequestProcessUtils" %><%--
  Created by IntelliJ IDEA.
  User: will
  Date: 2019/3/8
  Time: 16:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    System.out.println("song method");
    RequestProcessUtils.processSongRequest(request, out, application);
%>