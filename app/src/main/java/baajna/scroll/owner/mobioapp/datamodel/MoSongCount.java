package baajna.scroll.owner.mobioapp.datamodel;

/**
 * Created by Sajal on 5/5/2016.
 */
public class MoSongCount {
    private int songId,status;
    private String action,lastMod;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastMod() {
        return lastMod;
    }

    public void setLastMod(String lastMod) {
        this.lastMod = lastMod;
    }
}
