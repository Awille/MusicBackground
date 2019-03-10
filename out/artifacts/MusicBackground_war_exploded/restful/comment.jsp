<%@ page import="utils.RequestProcessUtils" %><%--
  Created by IntelliJ IDEA.
  User: will
  Date: 2019/3/10
  Time: 14:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("utf-8");
%>

<%
    System.out.println("comment method");
    RequestProcessUtils.processCommentRequest(request, out, application);
%>
