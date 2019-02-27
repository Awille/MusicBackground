package bean;

public class Comment {
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
}
