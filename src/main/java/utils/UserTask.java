package utils;

import bean.Message;
import bean.User;
import bean.requestmessage.RequestBean;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import commonconstant.CommonConstant;
import database.DbConnectManager;
import database.crud.UserCrud;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class UserTask implements Callable<Boolean> {
    private HttpServletRequest myRequest;
    private JspWriter myOut;

    public UserTask(HttpServletRequest myRequest, JspWriter myOut) {
        this.myRequest = myRequest;
        this.myOut = myOut;
    }

    @Override
    public Boolean call() throws Exception {
        DruidPooledConnection connection = DbConnectManager.getINSTANCE().getConnection();
        if (myRequest.getMethod().equalsIgnoreCase("GET")) {
            return queryUserByAccount(connection);
        } else if (myRequest.getMethod().equalsIgnoreCase("POST")) {
            return addUser(connection);
        } else if (myRequest.getMethod().equalsIgnoreCase("PUT")) {

        }
        return false;
    }

    private Boolean queryUserByAccount(DruidPooledConnection connection) {
        String account = myRequest.getParameter("account");
        User user = UserCrud.queryUserByAccount(account, connection);
        if (user != null) {
            try {
                myOut.print(JSON.toJSON(
                        new Message(CommonConstant.Result.SUCCESS_CODE,
                                CommonConstant.Result.SUCCESS_MSG,
                                user)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            try {
                myOut.print(JSON.toJSON(
                        new Message(CommonConstant.Result.FAIL_CODE,
                                "用户查询失败",
                                null)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private Boolean addUser(DruidPooledConnection connection) {
        StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        String body = null;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = myRequest.getReader();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            body = stringBuffer.toString();
            JSONObject first = JSON.parseObject(body);
            JSONObject second = JSON.parseObject(first.getString("data").toString());
            User user = JSON.parseObject(second.getString("user"), User.class);
            User result = UserCrud.addUser(user, connection);
            if (user != null) {
                myOut.print(JSON.toJSON(
                        new Message(CommonConstant.Result.SUCCESS_CODE,
                                CommonConstant.Result.SUCCESS_MSG,
                                user)));
                return true;
            } else {
                myOut.print(JSON.toJSON(
                        new Message(CommonConstant.Result.FAIL_CODE,
                                "用户添加失败",
                                null)));
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Boolean processPut(DruidPooledConnection connection) {
        StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        String body = null;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = myRequest.getReader();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            body = stringBuffer.toString();
            JSONObject first = JSON.parseObject(body);
            String service = first.getString("service");


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
