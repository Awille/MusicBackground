package database.crud;

import bean.Song;
import bean.UploadFile;
import com.alibaba.druid.pool.DruidPooledConnection;
import database.DbConnectManager;
import sun.misc.BASE64Decoder;
import utils.FileUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongCrud {
    /**
     * 添加歌曲
     * @param song
     * @param connection
     * @return 添加歌曲实例
     */
    public static Song addSong(Song song, Connection connection) {
        if (song == null) {
            return null;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO music.song (lyric_url, album_name, singer, name, avatar_url, resource_url, author, author_name) " +
                            "VALUE (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, song.getLyricUrl() == null ? "" : song.getLyricUrl());
            preparedStatement.setString(2, song.getAlbumName() == null ? "无" : song.getAlbumName());
            preparedStatement.setString(3, song.getSinger() == null ? "佚名" : song.getSinger());
            preparedStatement.setString(4, song.getName());
            preparedStatement.setString(5, song.getAvatarUrl());
            preparedStatement.setString(6, song.getResourceUrl());
            preparedStatement.setLong(7, song.getAuthor());
            preparedStatement.setString(8, song.getAuthorAccount() == null ? "" : song.getAuthorAccount());
            if (preparedStatement.executeUpdate() > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                //补充主键信息
                if (resultSet.next()) {
                    song.setSongId(resultSet.getLong(1));
                }
                return song;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<Song> querySongByAuthor(long author, DruidPooledConnection connection) {
        List<Song> songs = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT  * FROM music.song WHERE author = ?");
            preparedStatement.setLong(1, author);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Song song = new Song();
                song.setAuthor(resultSet.getLong("author"));
                song.setSongId(resultSet.getLong("song_id"));
                song.setLyricUrl(resultSet.getString("lyric_url"));
                song.setAlbumName(resultSet.getString("album_name"));
                song.setSinger(resultSet.getString("singer"));
                song.setName(resultSet.getString("name"));
                song.setResourceUrl(resultSet.getString("resource_url"));
                song.setAvatarUrl(resultSet.getString("avatar_url"));
                song.setAuthorAccount(resultSet.getString("author_account"));
                songs.add(song);
            }
            resultSet.close();
            return songs;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 查询歌曲
     * @param songId
     * @param connection
     * @return 查询歌曲实例
     */
    public static Song querySongBySongId(long songId, DruidPooledConnection connection) {
        Song song = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "select * from music.song where song_id = ?");
            preparedStatement.setLong(1, songId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                song = new Song();
                song.setSongId(resultSet.getLong("song_id"));
                song.setLyricUrl(resultSet.getString("lyric_url"));
                song.setAlbumName(resultSet.getString("album_name"));
                song.setSinger(resultSet.getString("singer"));
                song.setName(resultSet.getString("name"));
                song.setResourceUrl(resultSet.getString("resource_url"));
                song.setAvatarUrl(resultSet.getString("avatar_url"));
                song.setAuthor(resultSet.getLong("author"));
                song.setAuthorAccount(resultSet.getString("author_account"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return song;
    }

    /**
     * 根据关键词查找歌曲
     * @param word 关键词
     * @param connection
     * @return 歌曲列表
     */
    public static List<Song> queryBySongName(String word, DruidPooledConnection connection) {
        List<Song> songs = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM music.song WHERE name LIKE ? OR singer LIKE ? OR album_name LIKE ?");
            preparedStatement.setString(1, word);
            preparedStatement.setString(2, word);
            preparedStatement.setString(3, word);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Song song = new Song();
                song.setSongId(resultSet.getLong("song_id"));
                song.setLyricUrl(resultSet.getString("lyric_url"));
                song.setAlbumName(resultSet.getString("album_name"));
                song.setSinger(resultSet.getString("singer"));
                song.setName(resultSet.getString("name"));
                song.setResourceUrl(resultSet.getString("resource_url"));
                song.setAvatarUrl(resultSet.getString("avatar_url"));
                song.setAuthor(resultSet.getLong("author"));
                song.setAuthorAccount(resultSet.getString("author_account"));
                songs.add(song);
            }
            resultSet.close();
            return songs;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 更新歌曲信息
     * @param song
     * @param connection
     * @return 更新歌曲实例
     */
    public static Song updateSong(Song song, DruidPooledConnection connection) {
        if (song == null) {
            return null;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "update music.song set " +
                            "lyric_url = ?, album_name = ?, singer = ?, name = ? " +
                            "where song_id = ?");
            preparedStatement.setString(1, song.getLyricUrl());
            preparedStatement.setString(2, song.getAlbumName());
            preparedStatement.setString(3, song.getSinger());
            preparedStatement.setString(4, song.getName());
            preparedStatement.setLong(5, song.getSongId());
            if (preparedStatement.executeUpdate() > 0) {
                return song;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 删除歌曲
     * @param songId
     * @param connection
     * @return 删除结果
     */
    public static boolean deleteSong(long songId, DruidPooledConnection connection) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM music.song where song_id = ?");
            preparedStatement.setLong(1, songId);
            if (preparedStatement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 存储歌曲文件
     * @param uploadFile
     * @param servletContext
     * @return 返回文件名
     */
    public static String saveSongResource(UploadFile uploadFile, ServletContext servletContext) {
        return FileUtils.saveFile(uploadFile, servletContext, FileUtils.songPath, "song");
    }

    public static String saveLyricResource(UploadFile uploadFile, ServletContext servletContext) {
        return FileUtils.saveFile(uploadFile, servletContext, FileUtils.lyricPath, "lyric");
    }

    public static String saveSongAvatar(UploadFile uploadFile, ServletContext servletContext) {
        return FileUtils.saveFile(uploadFile, servletContext, FileUtils.songAvatarPath, "songavatar");
    }




}
