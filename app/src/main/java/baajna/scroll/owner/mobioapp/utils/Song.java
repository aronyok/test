package baajna.scroll.owner.mobioapp.utils;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Lee on 2015-03-04.
 */

public class Song {
    private boolean isStreaming;
    private int index;
    private long id;
    private String title;
    private String artist;
    private String album;
    private String uri;
    private String uri_albumArt;
    private Bitmap albumArt;

    public Song(boolean isSongStreaming, int songIndex, long songID, long albumID, String songTitle, String songArtist, String songAlbum, String songUri, String albumArtUri, Bitmap albumImage) {
        isStreaming = isSongStreaming;
        index = songIndex;
        id = songID;
        title = songTitle;
        artist = songArtist;
        album = songAlbum;
        uri = songUri;
        uri_albumArt = albumArtUri;
        albumArt = albumImage;

    }

    public boolean isStreaming() {
        return isStreaming;
    }

    public int getIndex() {
        return index;
    }

    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getUri() {
        return uri;
    }

    public String getAlbumArtUri() {
        return uri_albumArt;
    }

    public Bitmap getAlbumArt() {
        return albumArt;
    }

}