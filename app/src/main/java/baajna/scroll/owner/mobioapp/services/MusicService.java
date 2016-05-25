package baajna.scroll.owner.mobioapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.datamodel.MoSongCount;
import baajna.scroll.owner.mobioapp.utils.CommonFunc;
import baajna.scroll.owner.mobioapp.utils.Decryption;
import baajna.scroll.owner.mobioapp.utils.Globals;
import baajna.scroll.owner.mobioapp.utils.MyApp;
import baajna.scroll.owner.mobioapp.utils.SparkleApp;
import baajna.scroll.owner.mobioapp.utils.Utils;
import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.interfaces.IMusic;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {


    //notification id
    private static final int NOTIFY_ID = 1;
    public static String NORMAL_ACTION = "com.jewel.sparkle.music_player.action.normal";
    public static String MAIN_ACTION = "com.jewel.sparkle.music_player.action.menu_myplaylist";
    public static String PREV_ACTION = "com.jewel.sparkle.music_player.action.prev";
    public static String PLAY_ACTION = "com.jewel.sparkle.music_player.action.play";
    public static String PAUSE_ACTION = "com.jewel.sparkle.music_player.action.pause";
    public static String NEXT_ACTION = "com.jewel.sparkle.music_player.action.next";
    public static String STARTFOREGROUND_ACTION = "com.jewel.sparkle.music_player.action.start_foreground";
    public static String STOPFOREGROUND_ACTION = "com.jewel.sparkle.music_player.action.stop_foreground";


    private static int song_id;
    MoSong song;
    //media player
    public static MediaPlayer player;
    public static NotificationCompat.Builder builder;
    public static RemoteViews notificationView;
    public static Notification not;
    public static NotificationManager mNotificationManager;
    //song list
    public static ArrayList<MoSong> songs;
    //current position
    public static int songPosn;

    private int songId;

    public static boolean isCreated;
    //shuffle flag and random
    public static boolean shuffle = false, isRunning;
    //title of current song
    private static String songTitle = "";
    private static Random rand;
    private static IMusic iMusic;

    public static int playerState;
    public static final int STATE_NOT_READY = 1;
    public static final int STATE_PLAYING = 2;
    public static final int STATE_PAUSE = 3;
    public static final int STATE_STOP = 4;
    public static final int STATE_LOADING = 5;

    private static Context context;



    @Override
    public void onCreate() {
        //create the service
        super.onCreate();

        rand = new Random();
        context = this;
        //initialize
        initMusicPlayer();
        //prepareNotification();


    }

    //activity will bind to service
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("Jewel", "Started service");
        isCreated = true;
        if (player == null)
            initMusicPlayer();
        if (intent != null)
            if (intent.getAction().equals(MAIN_ACTION)) {
                if (!player.isPlaying()) {
                    playSong(songPosn);
                } else {
                    player.start();
                }
            } else if (intent.getAction().equals(PREV_ACTION)) {
                playPrev();

            } else if (intent.getAction().equals(PLAY_ACTION)) {
                if (player.isPlaying()) {
                    playPause();
                    notificationView.setImageViewResource(R.id.not_play, R.drawable.play_icon);
                    notificationView.setViewVisibility(R.id.not_stop, View.VISIBLE);
                    not.contentView = notificationView;
                    mNotificationManager.notify(NOTIFY_ID, not);
                } else {
                    player.start();
                    //callOnline();
                    notificationView.setViewVisibility(R.id.not_stop, View.GONE);
                    playerState = STATE_PLAYING;
                    notificationView.setImageViewResource(R.id.not_play, R.drawable.pause_icon);
                    not.contentView = notificationView;
                    mNotificationManager.notify(NOTIFY_ID, not);


                }
                if (iMusic != null) {
                    iMusic.onUpdate(player);
                }


            } else if (intent.getAction().equals(NEXT_ACTION)) {
                playNext();
            } else if (intent.getAction().equals(STARTFOREGROUND_ACTION)) {
                startForeground(1, not);
            } else if (intent.getAction().equals(STOPFOREGROUND_ACTION)) {
                isRunning = false;
                stopSelf();
            }


        return START_STICKY;
    }

    public static void setUpdateInterface(IMusic _iMusic) {

        iMusic = _iMusic;
    }

    //play a song
    public static void playSong(int pos) {
        if (player == null)
            initMusicPlayer();
        isRunning = false;
        playerState = STATE_NOT_READY;
        if (iMusic != null) {
            iMusic.onUpdate(player);
        }


        DbManager myDb = new DbManager(MyApp.getAppContext());
        songs = myDb.getSongs(DbManager.SQL_SONGS_PLAYLIST_RUNNING);
        myDb.close();

        songPosn = pos > songs.size() - 1 ? 0 : pos;
        player.reset();

        //get song
        if (songs != null && songs.size() > 0) {
            MoSong song = songs.get(songPosn);

            //get title
            songTitle = song.getTitle();

            if (song.getIsDownloaded() == 1) {
                try {
                    Decryption decryption = new Decryption();
                    byte[] data = decryption.decrypt(decryption.getAudioFileFromSdCard(songs.get(songPosn).getFileName()));
                    Log.e("SONG ID",":"+songs.get(songPosn).getId());
                    playMp3(data);
                    Log.e("SONG Name", ":" + songs.get(songPosn).getFileName());
                    saveSongStatus(songs.get(songPosn).getId(),"offline",0);

                } catch (Exception e) {
                    Log.d("Jewel", e.toString());
                    Toast.makeText(context, "File may be deleted.. Please download again..", Toast.LENGTH_LONG).show();
                }



            } else {
                //set the data source
                try {

                    player.setDataSource(MyApp.getAppContext(), Uri.parse(SparkleApp.getInstance().getSongUrl(song.getFileName())));

                    saveSongStatus(songs.get(songPosn).getId(), "live", 0);


                } catch (Exception e) {
                    Log.e("MUSIC SERVICE", "Error setting data source", e);
                }

                player.prepareAsync();
            }
        }


    }


    private static void playMp3(byte[] mp3SoundByteArray) {

        try {

            // create temp file that will hold byte array

            File tempMp3 = File.createTempFile("kurchina", "mp3", MyApp.getAppContext().getCacheDir());

            tempMp3.deleteOnExit();

            FileOutputStream fos = new FileOutputStream(tempMp3);

            fos.write(mp3SoundByteArray);

            fos.close();


            // Tried reusing instance of media player

            // but that resulted in system crashes...


            FileInputStream fis = new FileInputStream(tempMp3);

            player.setDataSource(fis.getFD());


            player.prepare();

            player.start();

        } catch (IOException ex) {


            ex.printStackTrace();

        }

    }

    //skip to previous track
    public static void playPrev() {
        if (player == null)
            return;
        isRunning = false;
        songPosn--;
        if (songPosn < 0) songPosn = songs.size() - 1;
        playSong(songPosn);

        if (iMusic != null) {
            iMusic.onUpdate(player);
        }
    }

    //skip to remoteNext
    public static void playNext() {
        if (player == null)
            return;
        isRunning = false;
        if (shuffle) {
            int newSong = songPosn;
            while (newSong == songPosn) {
                newSong = rand.nextInt(songs.size());
            }
            songPosn = newSong;
        } else {

            songPosn++;
            if (songPosn >= songs.size()) songPosn = 0;
        }
        playSong(songPosn);
        if (iMusic != null) {
            iMusic.onUpdate(player);
        }
    }

    public static void remoteStop() {
        isRunning = false;
        player.stop();
        player.release();
        player = null;
        mNotificationManager.cancel(NOTIFY_ID);
        if (iMusic != null) {
            iMusic.onUpdate(player);
        }
    }

    //playback methods
    public static int getPosn() {
        if (isRunning)
            return player.getCurrentPosition();
        return 0;
    }

    public static int getDur() {

        return player.getDuration();
    }

    public static boolean isPlaying() {
        if (player != null)
            return player.isPlaying();
        return false;
    }

    public static void playPause() {

        if (playerState == STATE_NOT_READY || playerState == STATE_STOP || playerState == 0)
            playSong(songPosn);
            //Log.d("Jewel","Call first: "+playerState);

        else if (player != null && player.isPlaying()) {
            isRunning = false;
            player.pause();
            playerState = STATE_PAUSE;

            //prepare notification
            notificationView.setImageViewResource(R.id.not_play, R.drawable.play_icon);
            notificationView.setViewVisibility(R.id.not_stop, View.VISIBLE);
            not.contentView = notificationView;
            mNotificationManager.notify(NOTIFY_ID, not);
        }
        else if (player != null && !player.isPlaying()) {
            if (notificationView == null)
                prepareNotification();

            //
            playerState = STATE_PLAYING;
            player.start();
            //playSong(songPosn);
            //isRunning = true;

            //prepare notification
            notificationView.setViewVisibility(R.id.not_stop, View.GONE);
            notificationView.setImageViewResource(R.id.not_play, R.drawable.pause_icon);
            not.contentView = notificationView;
            mNotificationManager.notify(NOTIFY_ID, not);
        }

        if (iMusic != null) {
            iMusic.onUpdate(player);
        }
    }

    public static void seek(int posn) {

        player.seekTo(posn);
    }

    public static void go() {
        player.start();
    }

    public static String getRunningSongTitle() {
        return songs.get(songPosn).getTitle();
    }

    public static String getRunningSongAlbum() {
        return (songs != null && isRunning) ? songs.get(songPosn).getArtist_name():" ";
    }

    public static  String getRunningSongImage(){
        Log.d("Sajal","Song : "+songs.get(songPosn).getImgUrl());
        return songs.get(songPosn).getImgUrl();

    }

    public static void setShuffle() {
        shuffle = !shuffle;
    }


    //set the song
    public void setSong(int songIndex) {
        songPosn = songIndex;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //check if playback has reached the end of a track
        if (player.getCurrentPosition() > 0) {
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        playerState = STATE_PLAYING;
        isRunning = true;
        mp.start();
        prepareNotification();

        if (iMusic != null) {
            iMusic.onUpdate(player);
        }


    }

    public static void initMusicPlayer() {

        //create player
        player = new MediaPlayer();
        //set player properties
        player.setWakeMode(MyApp.getAppContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners

        player.setOnPreparedListener((MediaPlayer.OnPreparedListener) context);
        player.setOnCompletionListener((MediaPlayer.OnCompletionListener) context);
        player.setOnErrorListener((MediaPlayer.OnErrorListener) context);
        player.setOnBufferingUpdateListener((MediaPlayer.OnBufferingUpdateListener) context);
    }

    //pass song list
    public void setList(ArrayList<MoSong> theSongs) {
        songs = theSongs;
    }

    @Override
    public void onDestroy() {
        if (songs != null) {
            new Utils().saveSharedPref(Globals.LAST_SONG_ID, songs.get(songPosn).getId() + "");
        }

        isRunning = false;
        isCreated = false;
        stopForeground(true);
        player.stop();
        player.release();
        player = null;
        playerState = STATE_STOP;
        mNotificationManager.cancel(NOTIFY_ID);
        if (iMusic != null) {
            iMusic.onUpdate(player);
        }

    }

    private static void prepareNotification() {

        builder = new NotificationCompat.Builder(MyApp.getAppContext());
        notificationView = new RemoteViews(MyApp.getAppContext().getPackageName(), R.layout.notification_view);
        notificationView.setTextViewText(R.id.songname_Text, "" + songTitle + " " + "is playing");


        Intent intent = new Intent(MyApp.getAppContext(), MainActivity.class);
        //intent.setAction(MAIN_ACTION);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyApp.getAppContext(), 0, intent, 0);


        Bitmap icon = BitmapFactory.decodeResource(MyApp.getAppContext().getResources(), R.drawable.button_songbar_pause);
        not = builder.setLargeIcon(icon)
                .setSmallIcon(R.drawable.button_songbar_pause)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentIntent(pendingIntent)
                .setContentText(songTitle)
                .build();


        not.contentView = notificationView;

        intent = new Intent(MyApp.getAppContext(), MusicService.class);
        intent.setAction(PREV_ACTION);
        PendingIntent pPrev = PendingIntent.getService(MyApp.getAppContext(), 0, intent, 0);

        intent = new Intent(MyApp.getAppContext(), MusicService.class);
        intent.setAction(PLAY_ACTION);
        PendingIntent pPlay = PendingIntent.getService(MyApp.getAppContext(), 0, intent, 0);

        intent = new Intent(MyApp.getAppContext(), MusicService.class);
        intent.setAction(NEXT_ACTION);
        PendingIntent pNext = PendingIntent.getService(MyApp.getAppContext(), 0, intent, 0);

        intent = new Intent(MyApp.getAppContext(), MusicService.class);
        intent.setAction(STOPFOREGROUND_ACTION);
        PendingIntent pStop = PendingIntent.getService(MyApp.getAppContext(), 0, intent, 0);

        notificationView.setOnClickPendingIntent(R.id.not_play, pPlay);

        notificationView.setOnClickPendingIntent(R.id.not_stop, pStop);

        notificationView.setOnClickPendingIntent(R.id.not_next, pNext);

        notificationView.setOnClickPendingIntent(R.id.not_prev, pPrev);
        mNotificationManager = (NotificationManager) MyApp.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFY_ID, not);

        //startForeground(NOTIFY_ID, not);
    }

    public static int getState() {
        if (player != null && !player.isPlaying() && playerState == 2) {
            playerState = 3;
        }

        return playerState;
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d("Jewel", " update : " + percent);
        if (iMusic != null) {
            iMusic.onUpdate(player);
        }
    }

    /*private void callOnline()  {
        Log.e("Music","found" );
        if (InternetConnectivity.isConnectedToInternet(this)) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            Log.e("Music","id:"+songs.get(songPosn).getId());
            JSONObject j=new JSONObject();
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("song_id",songs.get(songPosn).getId());
                jsonObject.put("user_app_id","124");
                jsonObject.put("action","download");
                jsonObject.put("sync_time", "2016-04-20 12:24:00");

                JSONArray jsonArray=new JSONArray();
                jsonArray.put(jsonObject);



                j.put("song_list",jsonArray);
                //Log.e("JSON", j.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.put("song_list", j.toString());
            client.post("http://52.89.156.64/index.php/api/songs_count", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.e("JSON______", response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.e("JSON______", responseString);
                }
            });

        }
    }*/

    private static void saveSongStatus(int id,String action,int status)  {
        DbManager db=new DbManager(MyApp.getAppContext());
        MoSongCount s=new MoSongCount();
        s.setSongId(id);
        s.setAction(action);
        s.setStatus(status);
        s.setLastMod(CommonFunc.getCurrentTime());
        db.addSongCount(s);
    }


}