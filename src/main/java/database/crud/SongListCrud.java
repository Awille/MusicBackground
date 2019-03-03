package database.crud;

import bean.Song;
import bean.songlist.SongList;
import bean.songlist.SongListBasicInfo;
import database.DatabaseConnectManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SongListCrud {
    /**
     * 增加歌曲
     * @param songList
     * @return 增加歌曲实例
     */
    public static SongList addSongList(SongList songList) {
        if (songList == null) {
            return null;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
                    "INSERT INTO music.songlist (user_id, name, avatar_url) VALUE (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, songList.getBasicInfo().getUserId());
            preparedStatement.setString(2, songList.getBasicInfo().getName());
            preparedStatement.setString(3, songList.getBasicInfo().getAvatarUrl());
            if (preparedStatement.executeUpdate() > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    songList.getBasicInfo().setSongListId(resultSet.getLong("song_list_id"));
                }
                resultSet.close();
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
     * 查询歌单基本信息
     * @param songListId
     * @return 歌单实例
     */
    public static SongList querySongListByUserId(long songListId) {
        SongList songList = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
                    "select * FROM music.songlist WHERE song_list_id = ?");
            preparedStatement.setLong(1, songListId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                songList = new SongList();
                SongListBasicInfo songListBasicInfo = new SongListBasicInfo();
                songListBasicInfo.setSongListId(songListId);
                songListBasicInfo.setUserId(resultSet.getLong("user_id"));
                songListBasicInfo.setAvatarUrl(resultSet.getString("avatar_url"));
                songListBasicInfo.setName(resultSet.getString("name"));
                songList.setBasicInfo(songListBasicInfo);
                return songList;
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
     * 根据歌单ID查找歌曲
     * @param songListId
     * @return 返回歌曲列表
     */
    public static List<Song> getSongsFromSongList(long songListId) {
        List<Song> songs = new ArrayList<Song>();
        boolean isEmpty = true;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
                    "SELECT music.song_id FROM music.songlist_song_relation WHERE song_list_id = ?");
            preparedStatement.setLong(1, songListId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                isEmpty = false;
                long songId = resultSet.getLong("song_id");
                Song song = SongCrud.querySongBySongId(songId);
                songs.add(song);
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
        if (!isEmpty) {
            return songs;
        } else {
            return null;
        }
    }

    /**
     * 得到用户的歌单信息
     * @param userId
     * @return 得到用户的歌单信息
     */
    public static List<SongList> getSongListsByUserId(long userId) {
        List<SongList> songLists = new ArrayList<SongList>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
                    "SELECT * FROM music.songlist WHERE user_id = ?");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                SongList songList = new SongList();
                SongListBasicInfo songListBasicInfo = new SongListBasicInfo();
                songListBasicInfo.setName(resultSet.getString("name"));
                songListBasicInfo.setAvatarUrl(resultSet.getString("avatar_url"));
                songListBasicInfo.setUserId(resultSet.getLong("user_id"));
                songListBasicInfo.setSongListId(resultSet.getLong("song_list_id"));
                songList.setBasicInfo(songListBasicInfo);
                songLists.add(songList);
            }
            resultSet.close();
            return songLists;
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
}
