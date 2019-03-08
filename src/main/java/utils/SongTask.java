package utils;

import bean.Message;
import bean.Song;
import bean.UploadFile;
import bean.User;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import commonconstant.CommonConstant;
import database.DbConnectManager;
import database.crud.SongCrud;
import database.crud.UserCrud;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

public class SongTask implements Callable<Boolean> {

    private HttpServletRequest myRequest;
    private JspWriter myOut;
    private ServletContext myContext;

    public SongTask(HttpServletRequest myRequest, JspWriter myOut, ServletContext myContext) {
        this.myRequest = myRequest;
        this.myOut = myOut;
        this.myContext = myContext;
    }
    @Override
    public Boolean call() throws Exception {
        DruidPooledConnection connection = DbConnectManager.getINSTANCE().getConnection();
        if (myRequest.getMethod().equalsIgnoreCase("GET")) {
            return querySong(connection);
        } else if (myRequest.getMethod().equalsIgnoreCase("POST")) {

        }
        connection.close();
        return false;
    }


    private boolean processInput(DruidPooledConnection connection) {
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
                case "201":
                    result = uploadSongResource(data, connection);
                    break;
                case "202":
                    result = uploadLyricResource(data, connection);
                    break;
                case "203":
                    result = addSong(connection);
                    break;
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean addSong (DruidPooledConnection connection) {
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
            Song song = JSON.parseObject(second.getString("song"), Song.class);
            Song result = SongCrud.addSong(song, connection);
            if (result != null) {
                myOut.print(JSON.toJSON(
                        new Message(CommonConstant.Result.SUCCESS_CODE,
                                CommonConstant.Result.SUCCESS_MSG,
                                result)));
                return true;
            } else {
                myOut.print(JSON.toJSON(
                        new Message(CommonConstant.Result.FAIL_CODE,
                                "歌曲添加失败",
                                null)));
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean uploadLyricResource(String data, DruidPooledConnection connection) throws IOException {
        JSONObject jsonObject = JSON.parseObject(data);
        UploadFile uploadFile = JSON.parseObject(jsonObject.get("uploadFile").toString(), UploadFile.class);
        String fileUrl = SongCrud.saveLyricResource(uploadFile, myContext);
        if (fileUrl != null) {
            myOut.print(JSON.toJSON(new Message(
                    CommonConstant.Result.SUCCESS_CODE,
                    CommonConstant.Result.SUCCESS_MSG,
                    fileUrl)));
            return true;
        } else {
            myOut.print(JSON.toJSON(new Message(
                    CommonConstant.Result.FAIL_CODE,
                    "上传歌词失败",
                    null)));
            return false;
        }
    }


    private boolean uploadSongResource(String data, DruidPooledConnection connection) throws IOException {
        JSONObject jsonObject = JSON.parseObject(data);
        UploadFile uploadFile = JSON.parseObject(jsonObject.get("uploadFile").toString(), UploadFile.class);
        String fileUrl = SongCrud.saveSongResource(uploadFile, myContext);
        if (fileUrl != null) {
            myOut.print(JSON.toJSON(new Message(
                    CommonConstant.Result.SUCCESS_CODE,
                    CommonConstant.Result.SUCCESS_MSG,
                    fileUrl)));
            return true;
        } else {
            myOut.print(JSON.toJSON(new Message(
                    CommonConstant.Result.FAIL_CODE,
                    "上传歌曲失败",
                    null)));
            return false;
        }
    }

    private boolean querySong(DruidPooledConnection connection) {
        String songIdStr = myRequest.getParameter("songId");
        String word = myRequest.getParameter("word");
        if (songIdStr != null) {
            long songId = 0;
            boolean flag = true;
            try {
                songId = Long.parseLong(songIdStr);
            } catch (NumberFormatException e) {
                flag = false;
                e.printStackTrace();
            }
            if (!flag) {
                try {
                    myOut.print(JSON.toJSON(new Message(
                            CommonConstant.Result.FAIL_CODE,
                            "查询失败",
                            null)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
            Song song = SongCrud.querySongBySongId(songId, connection);
            if (song != null) {
                try {
                    myOut.print(JSON.toJSON(new Message(
                            CommonConstant.Result.SUCCESS_CODE,
                            CommonConstant.Result.SUCCESS_MSG,
                            song)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                try {
                    myOut.print(JSON.toJSON(new Message(
                            CommonConstant.Result.FAIL_CODE,
                            "查询失败",
                            null)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        } else if (word != null) {
            List<Song> songs = SongCrud.queryBySongName(word, connection);
            if (songs != null) {
                try {
                    myOut.print(JSON.toJSON(new Message(
                            CommonConstant.Result.SUCCESS_CODE,
                            CommonConstant.Result.SUCCESS_MSG,
                            songs)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    myOut.print(JSON.toJSON(new Message(
                            CommonConstant.Result.FAIL_CODE,
                            "查询失败",
                            null)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                myOut.print(JSON.toJSON(new Message(
                        CommonConstant.Result.FAIL_CODE,
                        "查询失败",
                        null)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
