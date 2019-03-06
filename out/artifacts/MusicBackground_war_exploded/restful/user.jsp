<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.IOException" %>
<%@ page import="bean.requestmessage.RequestBean" %>
<%@ page import="database.DbConnectManager" %>
<%@ page import="com.alibaba.druid.pool.DruidPooledConnection" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="com.alibaba.fastjson.JSON" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="bean.User" %>
<%@ page import="database.crud.UserCrud" %>
<%@ page import="bean.Message" %>
<%@ page import="threadpoolservice.ThreadPoolService" %><%--
  Created by IntelliJ IDEA.
  User: will
  Date: 2019/3/6
  Time: 12:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%!
    public class MyRunnable implements Runnable {
        private JspWriter myOut;

        public MyRunnable(JspWriter jspWriter) {
            this.myOut = jspWriter;
        }
        @Override
        public void run() {
            int a = 1;
            try {
                myOut.print("没有用吗");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
%>

<%
    System.out.println("user method");


    //查看方法
//    Runnable task = new Runnable() {
//        @Override
//        public void run() {
//            //获取body中的数据
//            StringBuffer stringBuffer = new StringBuffer();
//            String line = null;
//            String body = null;
//            try {
//                BufferedReader bufferedReader = request.getReader();
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(line);
//                }
//                body = stringBuffer.toString();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            RequestBean requestBean = JSON.parseObject(body, RequestBean.class);
//            if (requestBean != null) {
//                System.out.println("hello");
//                System.out.println(JSON.toJSON(requestBean));
//            }
//            DruidPooledConnection connection = null;
//            try {
//                connection = DbConnectManager.getINSTANCE().getConnection();
//                if (request.getMethod().equalsIgnoreCase("GET")) {
//                    String account = request.getParameter("account");
//                    User user = UserCrud.queryUserByAccount(account, connection);
//                    if (user != null) {
//                        Message message = new Message();
//                        message.setCode("0");
//                        message.setMessage("查询成功");
//                        message.setData(user);
//                        try {
//                            out.print("test");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        Message message = new Message();
//                        message.setCode("101");
//                        message.setMessage("查询失败");
//                        try {
//                            out.print("test");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    };
    out.print("xxxxx");
    MyRunnable myRunnable = new MyRunnable(out);
    ThreadPoolService.requestExcutor.execute(myRunnable);


%>
