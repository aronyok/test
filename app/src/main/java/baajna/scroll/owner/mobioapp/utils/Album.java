package baajna.scroll.owner.mobioapp.utils;

import java.util.ArrayList;

/**
 * Created by Lee on 2015-04-06.
 */
public class Album {

    private String album;
    private String artist;
    private String uri_albumArt;
    private ArrayList<Song> songListForAlbum;

    public Album(ArrayList<Song> songList) {
        album = songList.get(0).getAlbum();
        artist = songList.get(0).getArtist();
        uri_albumArt = songList.get(0).getAlbumArtUri();
        songListForAlbum = new ArrayList<Song>();
        songListForAlbum.addAll(songList);
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumArtUri() {
        return uri_albumArt;
    }

    public ArrayList<Song> getSongListForAlbum() {
        return songListForAlbum;
    }
}
