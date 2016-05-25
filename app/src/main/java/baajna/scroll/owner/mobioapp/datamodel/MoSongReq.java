package baajna.scroll.owner.mobioapp.datamodel;

/**
 * Created by anuradha on 8/11/15.
 */
public class MoSongReq {
    private int id;
    private String songName;
    private String songDetails;


    public String getSongUrl() {
        return setSongUrl;
    }

    public void setSongUrl(String setSongUrl) {
        this.setSongUrl = setSongUrl;
    }

    private String setSongUrl;
    private String coverImgUrl;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongDetails(String songDetails) {
        this.songDetails = songDetails;
    }

    public String getSongDetails() {
        return songDetails;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }


}
