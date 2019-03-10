package utils;

import bean.Comment;
import bean.Message;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import commonconstant.CommonConstant;
import database.DbConnectManager;
import database.crud.CommentCrud;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
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
            result = processGet(connection);
        } else if (myRequest.getMethod().equalsIgnoreCase("POST")) {
            result = addComment(connection);
        } else if (myRequest.getMethod().equalsIgnoreCase("DELETE")) {
            result = deleteComment(connection);
        } else if (myRequest.getMethod().equalsIgnoreCase("PUT")) {
            result = prcocessPut(connection);
        }
        connection.close();
        return result;
    }

    private boolean prcocessPut(DruidPooledConnection connection) {
        StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        String body = null;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = myRequest.getReader();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer .append(line);
            }
            body = stringBuffer.toString();
            JSONObject first = JSON.parseObject(body);
            String service = first.getString("service");
            JSONObject second = JSON.parseObject(first.getString("data"));
            long commentId = second.getLong("commentId");
            boolean result = false;
            if (service.equals("401")) {
                result = CommentCrud.increLike(commentId, connection);
            } else {
                result = CommentCrud.increDislike(commentId, connection);
            }
            if (result) {
                myOut.print(JSON.toJSON(new Message(CommonConstant.Result.SUCCESS_CODE,
                        CommonConstant.Result.SUCCESS_MSG, null)));
            } else {
                myOut.print(JSON.toJSON(new Message(CommonConstant.Result.FAIL_CODE,
                        "操作失败", null)));
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

    private boolean deleteComment(DruidPooledConnection connection) {
        String commentIdStr = myRequest.getParameter("commentId");
        if (commentIdStr != null) {
            long commentId = Long.parseLong(commentIdStr);
            boolean result = CommentCrud.deleteComment(commentId, connection);
            try {
                if (result) {
                    myOut.print(JSON.toJSON(new Message(CommonConstant.Result.SUCCESS_CODE,
                            CommonConstant.Result.SUCCESS_MSG, null)));
                    return true;
                } else {
                    myOut.print(JSON.toJSON(new Message(CommonConstant.Result.SUCCESS_CODE,
                            "删除失败", null)));
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean addComment(DruidPooledConnection connection) {
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
            JSONObject second = JSON.parseObject(first.getString("data"));
            Comment comment = JSON.parseObject(second.getString("comment"), Comment.class);
            Comment result = CommentCrud.addComment(comment, connection);
            if (result != null) {
                myOut.print(JSON.toJSON(new Message(CommonConstant.Result.SUCCESS_CODE,
                        CommonConstant.Result.SUCCESS_MSG, result)));
                return true;
            } else {
                myOut.print(JSON.toJSON(new Message(CommonConstant.Result.FAIL_CODE,
                        "添加失败", result)));
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

    private boolean processGet(DruidPooledConnection connection) {
        String songIdStr = myRequest.getParameter("songId");
        String commentIdStr = myRequest.getParameter("commentId");
        String replyCommentIdStr = myRequest.getParameter("replyCommentId");
        if (songIdStr != null) {
            long songId = Long.parseLong(songIdStr);
            List<Comment> comments = CommentCrud.queryTopCommentsBySongId(songId, connection);
            try {
                if (comments != null) {
                    myOut.print(JSON.toJSON(new Message(CommonConstant.Result.SUCCESS_CODE,
                            CommonConstant.Result.SUCCESS_MSG, comments)));
                    return true;
                } else {
                    myOut.print(JSON.toJSON(new Message(CommonConstant.Result.FAIL_CODE,
                            "查询评论失败", null)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (commentIdStr != null) {
            long commentId = Long.parseLong(commentIdStr);
            Comment comment = CommentCrud.queryCommentByCommentId(commentId, connection);
            try {
                if (comment != null) {
                    myOut.print(JSON.toJSON(new Message(CommonConstant.Result.SUCCESS_CODE,
                            CommonConstant.Result.SUCCESS_MSG, comment)));
                    return true;
                } else {
                    myOut.print(JSON.toJSON(new Message(CommonConstant.Result.FAIL_CODE,
                            "查询失败", null)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (replyCommentIdStr != null) {
            long replyCommentId = Long.parseLong(replyCommentIdStr);
            List<Comment> comments = CommentCrud.querySecondComments(replyCommentId, connection);
            try {
                if (comments != null) {
                    myOut.print(JSON.toJSON(new Message(CommonConstant.Result.SUCCESS_CODE,
                            CommonConstant.Result.SUCCESS_MSG, comments)));
                    return true;
                } else {
                    myOut.print(JSON.toJSON(new Message(CommonConstant.Result.FAIL_CODE,
                            "查询回复评论失败", null)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
