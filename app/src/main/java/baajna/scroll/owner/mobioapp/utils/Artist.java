package baajna.scroll.owner.mobioapp.utils;

import java.util.ArrayList;

/**
 * Created by LunaMac on 15. 4. 7..
 */
public class Artist {
    private String artist;
    private String uri_albumArt;
    private ArrayList<Song> songListForArtist;
    private ArrayList<Album> albumListForArtist;

    public Artist(ArrayList<Song> songList, ArrayList<Album> albumList) {
        artist = songList.get(0).getArtist();
        uri_albumArt = songList.get(0).getAlbumArtUri();
        songListForArtist = new ArrayList<Song>();
        songListForArtist.addAll(songList);
        albumListForArtist = new ArrayList<Album>();
        albumListForArtist.addAll(albumList);

    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumArtUri() {
        return uri_albumArt;
    }

    public ArrayList<Song> getSongListForArtist() {
        return songListForArtist;
    }

    public ArrayList<Album> getAlbumListForArtist() {
        return albumListForArtist;
    }
}
