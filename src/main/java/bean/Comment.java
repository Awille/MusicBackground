package bean;

import java.io.Serializable;

public class Comment implements Serializable {
    /**
     * 评论ID
     */
    private long commentId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 所属用户ID
     */
    private long userId;
    /**
     * 所属歌曲ID
     */
    private long songId;
    /**
     * 评论时间
     */
    private String time;
    /**
     * 点赞数量
     */
    private long like;
    /**
     * 踩数量
     */
    private long  dislike;
    /**
     * 回复commentId
     */
    private long replyCommentId;
    /**
     * 评论等级
     * 0 首页 1 评论页 2 评论页内回复
     */
    private int commentLevel;
    /**
     * 回复数量
     */
    private long replyAmount;
    /**
     * 回复他的评论实体对象 在二级评论中才有
     */
    private Comment replyComment;

    private User user;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getLike() {
        return like;
    }

    public void setLike(long like) {
        this.like = like;
    }

    public long getDislike() {
        return dislike;
    }

    public void setDislike(long dislike) {
        this.dislike = dislike;
    }

    public long getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(long replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public int getCommentLevel() {
        return commentLevel;
    }

    public void setCommentLevel(int commentLevel) {
        this.commentLevel = commentLevel;
    }

    public long getReplyAmount() {
        return replyAmount;
    }

    public void setReplyAmount(long replyAmount) {
        this.replyAmount = replyAmount;
    }

    public Comment getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(Comment replyComment) {
        this.replyComment = replyComment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
