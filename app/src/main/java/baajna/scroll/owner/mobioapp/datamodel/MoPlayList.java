package baajna.scroll.owner.mobioapp.datamodel;

/**
 * Created by Jewel on 1/6/2016.
 */
public class MoPlayList {
    private int id,songId;
    private String modTime,name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getModTime() {
        return modTime;
    }

    public void setModTime(String modTime) {
        this.modTime = modTime;
    }
}
