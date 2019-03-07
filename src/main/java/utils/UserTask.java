package utils;

import bean.Message;
import bean.User;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import commonconstant.CommonConstant;
import database.DbConnectManager;
import database.crud.UserCrud;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
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
            if (user != null) {
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
        return false;
    }
}
