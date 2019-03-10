package utils;

import bean.Message;
import bean.Song;
import bean.UploadFile;
import bean.songlist.SongList;
import bean.songlist.SongListBasicInfo;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import commonconstant.CommonConstant;
import database.DbConnectManager;
import database.crud.SongListCrud;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

public class SongListTask implements Callable<Boolean> {
    private HttpServletRequest myRequest;
    private JspWriter myOut;
    private ServletContext myContext;

    public SongListTask(HttpServletRequest myRequest, JspWriter myOut, ServletContext myContext) {
        this.myRequest = myRequest;
        this.myOut = myOut;
        this.myContext = myContext;
    }


    @Override
    public Boolean call() throws Exception {
        DruidPooledConnection connection = DbConnectManager.getINSTANCE().getConnection();
        boolean result = false;
        if (myRequest.getMethod().equalsIgnoreCase("PUT")) {
            result = processPut(connection);
        } else if (myRequest.getMethod().equalsIgnoreCase("POST")) {
            result = processPost(connection);
        } else if (myRequest.getMethod().equalsIgnoreCase("GET")) {
            result = processGet(connection);
        } else if (myRequest.getMethod().equalsIgnoreCase("DELETE")) {

        }
        connection.close();
        return null;
    }

    private boolean processPut(DruidPooledConnection connection) {
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
            boolean result = false;
            switch (service) {
                case "301":
                    result = uploadSongListAvatar(data, connection);
                    break;
                case "302":
                    result = updateSongListName(data, connection);
                    break;
                case "303":

                    break;
                case "304":

                    break;
            }
        } catch (IOException e) {

        }
        return false;
    }

    private boolean updateSongListName(String data, DruidPooledConnection connection) {
        JSONObject jsonObject = JSON.parseObject(data);
        String name = jsonObject.getString("name");
        long songListId = jsonObject.getLong("songListId");
        boolean result = SongListCrud.updateSongListName(songListId, name, connection);
        try {
            if (result) {
                myOut.print(JSON.toJSON(new Message(CommonConstant.Result.SUCCESS_CODE,
                        CommonConstant.Result.SUCCESS_MSG, null)));
                return true;
            } else {
                myOut.print(JSON.toJSON(new Message(CommonConstant.Result.FAIL_CODE,
                        "修改失败", null)));
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean uploadSongListAvatar(String data, DruidPooledConnection connection) {
        JSONObject jsonObject = JSON.parseObject(data);
        UploadFile uploadFile = JSON.parseObject(jsonObject.get("uploadFile").toString(), UploadFile.class);
        boolean result = SongListCrud.uploadSongListAvatar(uploadFile, connection, myContext);
        try {
            if (result) {
                myOut.print(JSON.toJSON(new Message(CommonConstant.Result.SUCCESS_CODE,
                        CommonConstant.Result.SUCCESS_MSG, null)));
                return true;
            } else {
                myOut.print(JSON.toJSON(new Message(CommonConstant.Result.FAIL_CODE,
                        "上传失败", null)));
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean processPost(DruidPooledConnection connection) {
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
            String data = first.getString("data");
            JSONObject second = JSON.parseObject(data);
            String name = second.getString("name");
            long userId = second.getLong("userId");
            SongList songList = new SongList();
            SongListBasicInfo basicInfo = new SongListBasicInfo();
            basicInfo.setUserId(userId);
            basicInfo.setName(name);
            songList.setBasicInfo(basicInfo);
            SongList result = SongListCrud.addSongList(songList, connection);
            if (result != null) {
                myOut.print(JSON.toJSON(new Message(
                        CommonConstant.Result.SUCCESS_CODE,
                        CommonConstant.Result.SUCCESS_MSG,
                        result)));
            } else {
                myOut.print(JSON.toJSON(new Message(
                        CommonConstant.Result.FAIL_CODE,
                        CommonConstant.Result.FAIL_MSG, null)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean processGet(DruidPooledConnection connection) {
        String userIdStr = myRequest.getParameter("userId");
        String songListIdStr = myRequest.getParameter("songListId");
        if (userIdStr != null) {
            boolean flag = true;
            long userId = 0;
            try {
                userId = Long.parseLong(userIdStr);
            } catch (NumberFormatException e) {
                flag = false;
                e.printStackTrace();
            }
            try {
                if (!flag) {
                    myOut.print(JSON.toJSON(new Message(
                            CommonConstant.Result.FAIL_CODE,
                            CommonConstant.Result.FAIL_MSG, null)));
                }
                List<SongList> songLists = SongListCrud.getSongListsByUserId(userId, connection);
                if (songLists != null) {
                    myOut.print(JSON.toJSON(new Message(
                            CommonConstant.Result.SUCCESS_CODE,
                            CommonConstant.Result.SUCCESS_MSG,
                            songLists)));
                } else {
                    myOut.print(JSON.toJSON(new Message(
                            CommonConstant.Result.FAIL_CODE,
                            CommonConstant.Result.FAIL_MSG, null)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (songListIdStr != null) {
            boolean flag = true;
            long songListId = 0;
            try {
                songListId = Long.parseLong(songListIdStr);
            } catch (NumberFormatException e) {
                flag = false;
                e.printStackTrace();
            }
            try {
                if (!flag) {
                    myOut.print(JSON.toJSON(new Message(
                            CommonConstant.Result.FAIL_CODE,
                            CommonConstant.Result.FAIL_MSG, null)));
                }
                SongList songList = SongListCrud.querySongListBySongId(songListId, connection);
                if (songList != null) {
                    myOut.print(JSON.toJSON(new Message(
                            CommonConstant.Result.SUCCESS_CODE,
                            CommonConstant.Result.SUCCESS_MSG,
                            songList)));
                } else {
                    myOut.print(JSON.toJSON(new Message(
                            CommonConstant.Result.FAIL_CODE,
                            CommonConstant.Result.FAIL_MSG, null)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
