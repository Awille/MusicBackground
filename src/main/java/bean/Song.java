package bean;

import java.io.Serializable;

public class Song implements Serializable {
    /**
     * 歌曲ID
     */
    private long songId;
    /**
     * 歌词地址
     */
    private String lyricUrl;
    /**
     * 所属专辑名称
     */
    private String albumName;
    /**
     * 歌手
     */
    private String singer;
    /**
     * 歌曲名称
     */
    private String name;
    /**
     * 歌曲头像
     */
    private String avatarUrl;
    /**
     * 歌曲资源地址
     */
    private String resourceUrl;
    /**
     * 作者
     */
    private long author;

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getLyricUrl() {
        return lyricUrl;
    }

    public void setLyricUrl(String lyricUrl) {
        this.lyricUrl = lyricUrl;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
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

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public long getAuthor() {
        return author;
    }

    public void setAuthor(long author) {
        this.author = author;
    }
}
