package utils;

import com.alibaba.druid.pool.DruidPooledConnection;
import database.DbConnectManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CommentTask implements Callable<Boolean> {
    private HttpServletRequest myRequest;
    private JspWriter myOut;
    private ServletContext myContext;

    public CommentTask(HttpServletRequest myRequest, JspWriter myOut, ServletContext myContext) {
        this.myContext = myContext;
        this.myOut = myOut;
        this.myRequest = myRequest;
    }

    @Override
    public Boolean call() throws Exception {
        DruidPooledConnection connection = DbConnectManager.getINSTANCE().getConnection();
        boolean result = false;
        if (myRequest.getMethod().equalsIgnoreCase("GET")) {

        } else if (myRequest.getMethod().equalsIgnoreCase("POST")) {

        } else if (myRequest.getMethod().equalsIgnoreCase("DELETE")) {

        }
        connection.close();
        return result;
    }


}
