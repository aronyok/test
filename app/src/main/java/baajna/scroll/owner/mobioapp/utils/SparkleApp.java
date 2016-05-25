package baajna.scroll.owner.mobioapp.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;
import baajna.scroll.owner.mobioapp.datamodel.MoArtist;
import baajna.scroll.owner.mobioapp.datamodel.MoGenres;
import baajna.scroll.owner.mobioapp.datamodel.MoSongReq;
import baajna.scroll.owner.mobioapp.datamodel.MoUser;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;

import java.util.ArrayList;
import java.util.List;


public class SparkleApp {

    private static SparkleApp instance;

    public MoUser getMoUser() {
        return moUser;
    }

    public void setMoUser(MoUser moUser) {
        this.moUser = moUser;
    }

    private MoUser moUser;

    public MoSongReq getSongInfo() {
        return songInfo;
    }

    public void setSongReqInfo(MoSongReq songInfo) {
        this.songInfo = songInfo;
    }

    private MoSongReq songInfo;


    //http://mobioapp.net/apps/sparkle/ dev panel
    private final String base_url = Urls.BASE_URL + "index.php/api/create_user";
    private final String login_url = Urls.BASE_URL + "index.php/api/logins";
    private final String song_url = Urls.BASE_URL + "index.php/api/request_song";
    private static final String song_file_url = Urls.BASE_URL + "uploads/song_file/";


    public String getSingleSongPlayUrl() {
        return singleSongPlayUrl;
    }


    public void setSingleSongPlayUrl(String singleSongPlayUrl) {
        this.singleSongPlayUrl = singleSongPlayUrl;
    }

    private String singleSongPlayUrl;

    //Album
    private ArrayList<MoAlbum> albumList,topAlbumList;

    public ArrayList<MoAlbum> getAlbumList() {
        return albumList;
    }
    public ArrayList<MoAlbum> getTopAlbumList() {
        return topAlbumList;
    }
    public void setAlbumList(ArrayList<MoAlbum> albumList) {
        this.albumList = albumList;
    }
    public void setTopAlbumList(ArrayList<MoAlbum> topAlbumList) {
        this.topAlbumList = topAlbumList;
    }
    //Song
    private ArrayList<MoSong> songList,topSongList;

    public ArrayList<MoSong> getSongList() {
        return songList;
    }

    public void setSongList(ArrayList<MoSong> songList) {
        this.songList = songList;
    }

    public ArrayList<MoSong> getTopSongList() {
        return topSongList;
    }

    public void setTopSongList(ArrayList<MoSong> topSongList) {
        this.topSongList = topSongList;
    }

    //Artist
    private ArrayList<MoArtist> artistList,topArtistList,trendArtistList;

    public ArrayList<MoArtist> getArtistList() {
        return artistList;
    }

    public void setArtistList(ArrayList<MoArtist> artistList) {
        this.artistList = artistList;
    }

    public ArrayList<MoArtist> getTopArtistList() {
        return topArtistList;
    }

    public void setTopArtistList(ArrayList<MoArtist> topArtistList) {
        this.topArtistList = topArtistList;
    }

    public ArrayList<MoArtist> getTrendArtistList() {
        return trendArtistList;
    }

    public void setTrendArtistList(ArrayList<MoArtist> trendArtistList) {
        this.trendArtistList = trendArtistList;
    }

    //Genre

    private ArrayList<MoGenres> genreList;

    public ArrayList<MoGenres> getGenreList() {
        return genreList;
    }

    public void setGenreList(ArrayList<MoGenres> genreList) {
        this.genreList = genreList;
    }



    private SparkleApp() {

    }



    public static SparkleApp getInstance() {
        if (instance == null) {
            instance = new SparkleApp();
        }
        return instance;
    }
    public String getSongUrl(String url){
        return song_file_url+url;
    }

    public static void destroyInstance() {
        instance = null;
    }

    public String getBase_url() {
        return base_url;
    }

    public String getLogin_url() {
        return login_url;
    }

    //Song request url
    public String getSong_url() {
        return song_url;
    }

    public void openAlert(String title, final Activity activity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setTitle(title + " decision");
        alertDialogBuilder.setMessage("Please turn on your internet connection");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // go to a new activity of the app
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivity(intent);
            }
        });
        // set negative button: No message
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // cancel the alert box and put a Toast to the user
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }

    public boolean isEmailValid(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isServiceRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : services) {
            if (task.baseActivity.getClassName().equals("SplashActivity")
                    || task.baseActivity.getClassName().equals("MainActivity")) {
                return true;
            }
        }

        return false;

    }

}
