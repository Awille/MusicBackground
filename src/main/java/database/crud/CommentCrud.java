package database.crud;


import bean.Comment;
import database.DbConnectManager;

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
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
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

    /**
     * 更新评论数量
     * @param replyCommentId 回复评论ID
     * @return 是否更新成功
     */
    private static boolean updateReplyAmount(long replyCommentId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
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

    /**
     * 获得顶级评论
     * @param songId 歌曲ID
     * @return 评论列表
     */
    public static List<Comment> queryTopCommentsBySongId(long songId) {
        List<Comment> comments = new ArrayList<Comment>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
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

    /**
     * 根据评论ID 查询评论
     * @param commentId
     * @return 评论
     */
    public static Comment queryCommentByCommentId(long commentId) {
        Comment comment= null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
                    "SELECT * FROM music.comment WHERE comment_id = ?");
            preparedStatement.setLong(1, commentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                comment = new Comment();
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
        return comment;
    }


    /**
     * 获得二级评论 评中评
     * @param commentId
     * @return 评论中的评论列表
     */
    public static List<Comment> querySecondComments(long commentId) {
        List<Comment> comments = new ArrayList<Comment>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
                    "SELECT * FROM music.comment WHERE reply_comment_id = ? AND comment_level = 1");
            preparedStatement.setLong(1, commentId);
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
                //此条评论下还有评论
                if (comment.getReplyCommentId() != 0) {
                    Comment commentInComment = queryCommentByCommentId(comment.getReplyCommentId());
                    if (commentInComment != null) {
                        comment.setReplyComment(commentInComment);
                    }
                }
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

    /**
     * 删除评论 非真正意义上的删除 只是将评论的内容设为null
     * @param commentId
     * @return 删除结果
     */
    public static boolean deleteComment(long commentId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DbConnectManager.getINSTANCE().getConnection().prepareStatement(
                    "UPDATE music.comment SET content = NULL WHERE comment_id = ?");
            preparedStatement.setLong(1, commentId);
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
