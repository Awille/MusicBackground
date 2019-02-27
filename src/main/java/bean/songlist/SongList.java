package bean.songlist;

import bean.Song;

import java.util.List;

public class SongList {
    /**
     * 歌单基础信息
     */
    private SongListBasicInfo basicInfo;
    /**
     * 歌曲列表
     */
    private List<Song> songs;

    public SongListBasicInfo getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(SongListBasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
