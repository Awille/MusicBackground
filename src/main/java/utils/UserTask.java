package utils;

import bean.Message;
import bean.UploadFile;
import bean.User;
import bean.requestmessage.RequestBean;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import commonconstant.CommonConstant;
import database.DbConnectManager;
import database.crud.UserCrud;

import javax.servlet.ServletContext;
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
    private ServletContext myContext;

    public UserTask(HttpServletRequest myRequest, JspWriter myOut, ServletContext myContext) {
        this.myRequest = myRequest;
        this.myOut = myOut;
        this.myContext = myContext;
    }

    @Override
    public Boolean call() throws Exception {
        DruidPooledConnection connection = DbConnectManager.getINSTANCE().getConnection();
        boolean result = false;
        if (myRequest.getMethod().equalsIgnoreCase("GET")) {
            result = queryUserByAccount(connection);
        } else if (myRequest.getMethod().equalsIgnoreCase("POST")) {
            result = addUser(connection);
        } else if (myRequest.getMethod().equalsIgnoreCase("PUT")) {
            result = processPut(connection);
        }
        connection.close();
        return result;
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
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 用户更新服务
     * 101 更改用户信息
     * 102 更改密码
     * 103 上传/更改头像
     * @param connection
     * @return
     */
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
            String data = first.getString("data");
            Boolean result = false;
            switch (service) {
                case "101":
                    result = updateUserInfo(data, connection);
                    break;
                case "102":
                    result = changePassword(data, connection);
                    break;
                case "103":
                    result = modifyUserAvatar(data, connection);
                    break;
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private Boolean updateUserInfo(String data, DruidPooledConnection connection) throws IOException{
        JSONObject jsonObject = JSON.parseObject(data);
        User user = JSON.parseObject(jsonObject.getString("user"), User.class);
        User result = UserCrud.updateUser(user, connection);
        if (result != null) {
            myOut.print(JSON.toJSON(
                    new Message(CommonConstant.Result.SUCCESS_CODE,
                            CommonConstant.Result.SUCCESS_MSG,
                            user)));
            return true;
        } else {
            myOut.print(JSON.toJSON(
                    new Message(CommonConstant.Result.FAIL_CODE,
                            "用户信息更新失败",
                            null)));
            return false;
        }
    }

    private Boolean changePassword(String data, DruidPooledConnection connection) throws IOException {
        JSONObject jsonObject = JSON.parseObject(data);
        User user = JSON.parseObject(jsonObject.getString("user"), User.class);
        boolean result = UserCrud.changePassword(user, connection);
        if (result) {
            myOut.print(JSON.toJSON(
                    new Message(CommonConstant.Result.SUCCESS_CODE,
                            CommonConstant.Result.SUCCESS_MSG,
                            null)));
            return true;
        } else {
            myOut.print(JSON.toJSON(
                    new Message(CommonConstant.Result.FAIL_CODE,
                            "密码修改失败",
                            null)));
            return false;
        }
    }

    private Boolean modifyUserAvatar(String data, DruidPooledConnection connection) throws IOException{
        JSONObject jsonObject = JSON.parseObject(data);
        UploadFile uploadFile = JSON.parseObject(jsonObject.get("uploadFile").toString(), UploadFile.class);
        boolean result = UserCrud.uploadUserAvatar(uploadFile, connection, myContext);
        if (result) {
            myOut.print(JSON.toJSON(
                    new Message(CommonConstant.Result.SUCCESS_CODE,
                            CommonConstant.Result.SUCCESS_MSG,
                            null)));
            return true;
        } else {
            myOut.print(JSON.toJSON(
                    new Message(CommonConstant.Result.FAIL_CODE,
                            "图片上传失败",
                            null)));
            return false;
        }
    }
}
