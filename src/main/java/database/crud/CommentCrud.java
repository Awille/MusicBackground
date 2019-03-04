package database.crud;


import bean.Comment;
import database.DatabaseConnectManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CommentCrud {
    /**
     * 添加评论
     * @param comment
     * @return 添加评论实例
     */
    public static Comment addComment(Comment comment) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
                    "INSERT INTO music.comment (content, user_id, song_id, reply_comment_id, `like`, dislike) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, comment.getContent());
            preparedStatement.setLong(2, comment.getUserId());
            preparedStatement.setLong(3, comment.getSongId());
            preparedStatement.setLong(4, comment.getReplyCommentId());
            preparedStatement.setLong(5, comment.getLike());
            preparedStatement.setLong(6, comment.getDislike());
            if (preparedStatement.executeUpdate() > 0) {
                //更新成功后 添加相关评论数量
                if (comment.getReplyCommentId() != 0) {
                    updateReplyAmount(comment.getReplyCommentId());
                }
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    comment.setCommentId(resultSet.getLong("comment_id"));
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

    private static boolean updateReplyAmount(long replyCommentId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
                    "UPDATE music.comment SET reply_amount = reply_amount + 1 " +
                            "WHERE comment_id = ?");
            preparedStatement.setLong(1, replyCommentId);
            if (preparedStatement.executeUpdate() > 0) {
                return true;
            } else {
                return false;
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

    private static List<Comment> queryTopCommentsBySongId(long songId) {
        List<Comment> comments = new ArrayList<Comment>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
                    "SELECT * FROM music.comment WHERE song_id = ? AND  comment_level = 0");
            preparedStatement.setLong(1, songId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setCommentId(resultSet.getLong("comment_id"));
                comment.setContent(resultSet.getString("content"));
                comment.setUserId(resultSet.getLong("user_id"));
                comment.setCommentLevel(resultSet.getInt("comment_level"));
                comment.setSongId(resultSet.getLong("song_id"));
                comment.setTime(resultSet.getDate("time").toString());
                comment.setLike(resultSet.getLong("like"));
                comment.setDislike(resultSet.getLong("dislike"));
                comment.setReplyCommentId(resultSet.getLong("reply_comment_id"));
                comment.setReplyAmount(resultSet.getLong("reply_amount"));

                comments.add(comment);
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
        return comments;
    }

}
