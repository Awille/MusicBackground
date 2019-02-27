package bean.songlist;

public class SongListBasicInfo {
    /**
     * 歌单ID
     */
    private long songListId;
    /**
     * 用户ID
     */
    private long userId;
    /**
     * 歌单名称
     */
    private String name;
    /**
     * 歌单头像
     */
    private String avatarUrl;

    public long getSongListId() {
        return songListId;
    }

    public void setSongListId(long songListId) {
        this.songListId = songListId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
