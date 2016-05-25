package baajna.scroll.owner.mobioapp.utils;

import android.net.Uri;

import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;

import java.util.ArrayList;

/**
 * Created by Lee on 2015-03-08.
 */
public class Globals {
    public static ArrayList<MoSong> playlist = new ArrayList<MoSong>();
    public static ArrayList<MoSong> titlelist = new ArrayList<MoSong>();
    public static ArrayList<Album> albumlist = new ArrayList<Album>();
    public static ArrayList<Artist> artistlist = new ArrayList<Artist>();
    public static ArrayList<MoSong> playlistSong = new ArrayList<MoSong>();
    public static ArrayList<MoAlbum> albumslist=new ArrayList<MoAlbum>();
    public static ArrayList<MoAlbum> topalbumlist=new ArrayList<MoAlbum>();

    public static ArrayList<Integer>deletedSongIds=new ArrayList<>();
    //public static int songNumber;
    public static final String DOWNLOAD_FOLDER="/Sparkle/";
    public static String playlistSubtitle;
    public static Uri songUri;
    public static int backToTitleFragmentDirect = 1;
    public static final String ACTION_PLAYER_PLAY = "com.example.app.ACTION_PLAYER_PLAY";
    public static final String ACTION_PLAYER_EXIT = "com.example.app.ACTION_PLAYER_EXIT";
    public static final String ACTION_PLAYER_NEXT = "com.example.app.ACTION_PLAYER_NEXT";
    public static final String ACTION_PLAYER_PREV = "com.example.app.ACTION_PLAYER_PREV";

    public static final String APP_NAME = "Sparkle";
    public static final String LAST_SONG_ID="last_song_id";
    public static final String USER_LOGIN="user_login";
    public static final String GET_START="get_start";


    public static final int VIEW_ID_ARTST = 1;
    public static final int VIEW_ID_ALBUM = 2;
    public static final int VIEW_ID_NEWRELEASE = 3;

    public static final int TYPE_EXP_ALBUM=1;
    public static final int TYPE_EXP_PLAYLIST=2;




    public static int releaseDateLimit = 7*365;


}
