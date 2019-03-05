package database.crud;

import bean.Song;
import bean.songlist.SongList;
import bean.songlist.SongListBasicInfo;
import com.alibaba.druid.pool.DruidPooledConnection;
import database.DbConnectManager;

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
     * @param connection
     * @return 增加歌曲实例
     */
    public static SongList addSongList(SongList songList, DruidPooledConnection connection) {
        if (songList == null) {
            return null;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
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
     * @param connection
     * @return 歌单实例
     */
    public static SongList querySongListByUserId(long songListId, DruidPooledConnection connection) {
        SongList songList = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
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
     * @param connection
     * @return 返回歌曲列表
     */
    public static List<Song> getSongsFromSongList(long songListId, DruidPooledConnection connection) {
        List<Song> songs = new ArrayList<Song>();
        boolean isEmpty = true;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
                    "SELECT music.song_id FROM music.songlist_song_relation WHERE song_list_id = ?");
            preparedStatement.setLong(1, songListId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                isEmpty = false;
                long songId = resultSet.getLong("song_id");
                Song song = SongCrud.querySongBySongId(songId, connection);
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
     * @param connection
     * @return 得到用户的歌单信息
     */
    public static List<SongList> getSongListsByUserId(long userId, DruidPooledConnection connection) {
        List<SongList> songLists = new ArrayList<SongList>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
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


    /**
     * 删除歌单
     * 1、先缓存关系表中的歌单 2、先删除关系表中的歌单记录 3、删除歌单
     * 任何一步出现错误，都要进行回退数据库
     * @param songListId 歌单ID
     * @param connection
     * @return 删除歌单是否成功
     */
    public static boolean deleteSongList(long songListId, DruidPooledConnection connection) {
        //先缓存关系表中的歌单
        List<Long> songIdList = getSongIdsBySongListId(songListId, connection);
        //回退失败的数据
        List<Long> failBackSongId = new ArrayList<Long>();
        if (songIdList == null) {
            return false;
        }
        if (!deleteSongListInRelation(songListId, connection)) {
            return false;
        }
        //删除歌单失败
        if (!deleteSongListDirectly(songListId, connection)) {
            //用缓存回退数据
            for (Long songId : songIdList) {
                if (!addSongToSongList(songId, songListId, connection)) {
                    failBackSongId.add(songId);
                }
            }
            //如果有失败的回退数据
            if (failBackSongId.size() > 0) {

            }
            return false;
        }
        return true;
    }

    /**
     * 直接删除歌单
     * @param songListId
     * @param connection
     * @return
     */
    private static boolean deleteSongListDirectly(long songListId, DruidPooledConnection connection) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
                    "DELETE FROM music.songlist WHERE song_list_id = ?");
            preparedStatement.setLong(1, songListId);
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
     * 通过歌单ID查找歌曲
     * @param songListId
     * @param connection
     * @return
     */
    public static List<Long> getSongIdsBySongListId(long songListId, DruidPooledConnection connection) {
        List<Long> userIdList = new ArrayList<Long>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
                    "SELECT song_id FROM music.songlist_song_relation WHERE song_list_id = ?");
            preparedStatement.setLong(1, songListId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userIdList.add(resultSet.getLong("song_id"));
            }
            resultSet.close();
            return userIdList;
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
     * 在歌单 歌曲关信息表中删除
     * @param songListId
     * @param connection
     * @return 删除结果
     */
    public static boolean deleteSongListInRelation(long songListId, DruidPooledConnection connection) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
                    "DELETE FROM music.songlist_song_relation WHERE song_list_id = ?");
            preparedStatement.setLong(1, songListId);
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
     * 歌单中添加歌曲
     * @param songId
     * @param songListId
     * @param connection
     * @return 添加结果
     */
    public static boolean addSongToSongList(long songId, long songListId, DruidPooledConnection connection) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
                    "INSERT INTO music.songlist_song_relation (song_list_id, song_id) VALUE (?, ?)");
            preparedStatement.setLong(1, songListId);
            preparedStatement.setLong(2, songId);
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
