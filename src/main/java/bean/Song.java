package bean;

public class Song {
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
}
