package database.crud;

import bean.Song;
import database.DbConnectManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SongCrud {
    /**
     * 添加歌曲
     * @param song
     * @return 添加歌曲实例
     */
    public static Song addSong(Song song) {
        if (song == null) {
            return null;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
                    "INSERT INTO music.song (lyric_url, album_name, singer, name) " +
                            "VALUE (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, song.getLyricUrl());
            preparedStatement.setString(2, song.getAlbumName());
            preparedStatement.setString(3, song.getSinger());
            preparedStatement.setString(4, song.getName());
            if (preparedStatement.executeUpdate() > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                //补充主键信息
                if (resultSet.next()) {
                    song.setSongId(resultSet.getLong("song_id"));
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

    /**
     * 查询歌曲
     * @param songId
     * @return 查询歌曲实例
     */
    public static Song querySongBySongId(long songId) {
        Song song = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
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
     * 更新歌曲信息
     * @param song
     * @return 更新歌曲实例
     */
    public static Song updateSong(Song song) {
        if (song == null) {
            return null;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
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
     * @return 删除结果
     */
    public static boolean deleteSong(long songId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
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




}
