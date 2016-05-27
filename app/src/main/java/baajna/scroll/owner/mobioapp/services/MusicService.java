package baajna.scroll.owner.mobioapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.mobioapp.baajna.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.datamodel.MoSongCount;
import baajna.scroll.owner.mobioapp.interfaces.IMusic;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.utils.CommonFunc;
import baajna.scroll.owner.mobioapp.utils.Decryption;
import baajna.scroll.owner.mobioapp.utils.Globals;
import baajna.scroll.owner.mobioapp.utils.MyApp;
import baajna.scroll.owner.mobioapp.utils.SparkleApp;
import baajna.scroll.owner.mobioapp.utils.Utils;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {


    public static final int STATE_NOT_READY = 1;
    public static final int STATE_PLAYING = 2;
    public static final int STATE_PAUSE = 3;
    public static final int STATE_STOP = 4;
    public static final int STATE_LOADING = 5;
    //notification id
    private static final int NOTIFY_ID = 1;
    public static IMusic iMusic;

    //action
    public static String NORMAL_ACTION = "com.jewel.sparkle.music_player.action.normal";
    public static String MAIN_ACTION = "com.jewel.sparkle.music_player.action.menu_myplaylist";
    public static String PREV_ACTION = "com.jewel.sparkle.music_player.action.prev";
    public static String PLAY_ACTION = "com.jewel.sparkle.music_player.action.play";
    public static String PAUSE_ACTION = "com.jewel.sparkle.music_player.action.pause";
    public static String NEXT_ACTION = "com.jewel.sparkle.music_player.action.next";
    public static String STARTFOREGROUND_ACTION = "com.jewel.sparkle.music_player.action.start_foreground";
    public static String STOPFOREGROUND_ACTION = "com.jewel.sparkle.music_player.action.stop_foreground";
    //media player
    public static MediaPlayer player;
    //song list
    public static ArrayList<MoSong> songs;
    //current position
    public static int songPosn;
    //shuffle flag and random
    public static boolean shuffle = false, isRunning;
    public static int playerState;
    private  NotificationCompat.Builder builder;
    private  RemoteViews notificationView;
    private  Notification not;
    private  NotificationManager mNotificationManager;
    private static boolean isCreated;
    //title of current song
    private static String songTitle = "";
    private static Random rand;
    private static MoSong runningSong;

    private static Context context;

    private IBinder mBinder = new MyBinder();


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
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("Service", "Started service");

        if (player == null)
            initMusicPlayer();
        if (intent != null && intent.getAction() != null) {


            if (intent.getAction().equals(MAIN_ACTION)) {
                Log.e("Jewel", "S-MAIN");
                if (!player.isPlaying()) {
                    playSong(songPosn);
                } else {
                    player.start();
                }
            } else if (intent.getAction().equals(PREV_ACTION)) {
                Log.e("Jewel", "S-REV");
                playPrev();
                modifyNotification();
            } else if (intent.getAction().equals(PLAY_ACTION)) {
                Log.e("Jewel", "S-PLAY-pause");

                if (player.isPlaying()) {
                    playPause();

                } else {
                    player.start();
                    playerState = STATE_PLAYING;


                }
                modifyNotification();

            } else if (intent.getAction().equals(NEXT_ACTION)) {
                Log.e("Jewel", "S-NEXT");
                playNext();
                modifyNotification();
            } else if (intent.getAction().equals(STARTFOREGROUND_ACTION)) {
                Log.e("Jewel", "S-START");
                if(not!=null)
                startForeground(1, not);
            } else if (intent.getAction().equals(STOPFOREGROUND_ACTION)) {
                Log.e("Jewel", "S-END");
                isRunning = false;
               stopForeground(true);
                mNotificationManager.cancel(NOTIFY_ID);
                //System.exit(0);
            } else if (intent.getAction().equals(NORMAL_ACTION)) {
                Log.e("Jewel", "S-normal");

            }

        } else {
            Log.e("Jewel", "S-Nothing");

        }


        return START_STICKY;
    }

    public void setUpdateInterface(IMusic _iMusic) {

        iMusic = _iMusic;
    }

    //media player
    public void playSong(int pos) {
        Log.e("Jewel", "S-PLAYSONG");
        if (player == null)
            initMusicPlayer();
        isRunning = false;
        playerState = STATE_NOT_READY;
        if (iMusic != null) {
            iMusic.onUpdate(player, runningSong);
        }


        DbManager myDb = new DbManager(MyApp.getAppContext());
        songs = myDb.getSongs(DbManager.SQL_SONGS_PLAYLIST_RUNNING);
        myDb.close();

        songPosn = pos > songs.size() - 1 ? 0 : pos;
        player.reset();

        //get song
        if (songs != null && songs.size() > 0) {
            runningSong = songs.get(songPosn);

            //get title
            songTitle = runningSong.getTitle();

            if (runningSong.getIsDownloaded() == 1) {
                try {
                    Decryption decryption = new Decryption();
                    byte[] data = decryption.decrypt(decryption.getAudioFileFromSdCard(songs.get(songPosn).getFileName()));
                    Log.e("SONG ID", ":" + songs.get(songPosn).getId());
                    playMp3(data);
                    Log.e("SONG Name", ":" + songs.get(songPosn).getFileName());
                    saveSongStatus(songs.get(songPosn).getId(), "offline", 0);

                } catch (Exception e) {
                    Log.d("Jewel", e.toString());
                    Toast.makeText(context, "File may be deleted.. Please download again..", Toast.LENGTH_LONG).show();
                }


            } else {
                //set the data source
                try {

                    player.setDataSource(MyApp.getAppContext(), Uri.parse(SparkleApp.getInstance().getSongUrl(runningSong.getFileName())));

                    saveSongStatus(songs.get(songPosn).getId(), "live", 0);


                } catch (Exception e) {
                    Log.e("MUSIC SERVICE", "Error setting data source", e);
                }

                player.prepareAsync();
            }
        }


    }

    private void playMp3(byte[] mp3SoundByteArray) {

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
    public void playPrev() {
        if (player == null)
            return;

        isRunning = false;
        songPosn--;
        if (songPosn < 0) songPosn = songs.size() - 1;
        playSong(songPosn);

        if (iMusic != null) {
            iMusic.onUpdate(player, runningSong);
        }
    }

    //skip to remoteNext
    public void playNext() {
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
            iMusic.onUpdate(player, runningSong);
        }
    }

    public void playPause() {
        Log.e("SERVICE", "Play/pause");

        if (playerState == STATE_NOT_READY || playerState == STATE_STOP || playerState == 0) {
            playSong(songPosn);
            Log.e("Jewel", "Call first: " + playerState);
        } else if (player != null && player.isPlaying()) {
            Log.e("Jewel", "Call s: " + playerState);
            isRunning = false;
            player.pause();
            playerState = STATE_PAUSE;

        } else if (player != null && !player.isPlaying()) {
            if (notificationView == null)


                //
                playerState = STATE_PLAYING;
            player.start();

        }

        if (iMusic != null) {
            iMusic.onUpdate(player, runningSong);
        }
    }

    public void seek(int posn) {

        player.seekTo(posn);
    }

    public String getRunningSongTitle() {
        return songs.get(songPosn).getTitle();
    }

    public String getRunningSongAlbum() {
        return (songs != null && isRunning) ? songs.get(songPosn).getArtist_name() : " ";
    }

    public void setShuffle() {
        shuffle = !shuffle;
    }

    //set the song
    public void setSong(int songIndex) {
        Log.e("SERVICE", "cal");
        songPosn = songIndex;
    }

    //media player override methods

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
        Log.e("MUSIC", "on prepare");
        playerState = STATE_PLAYING;
        isRunning = true;

        mp.start();
        prepareNotification();

        if (iMusic != null) {
            iMusic.onUpdate(player, runningSong);
        }


    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d("Jewel", " update : " + percent);
        if (iMusic != null) {
            iMusic.onUpdate(player, runningSong);
        }
    }

    public void initMusicPlayer() {

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
    public void setSongList(ArrayList<MoSong> theSongs) {
        songs = theSongs;
    }

    @Override
    public void onDestroy() {
        Log.e("SERVICE", "destroy");
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
            iMusic.onUpdate(player, runningSong);
        }

    }

    private void prepareNotification() {
        Log.e("NOT", "call pre");
        builder = new NotificationCompat.Builder(MyApp.getAppContext());
        notificationView = new RemoteViews(MyApp.getAppContext().getPackageName(), R.layout.notification_view);

        notificationView.setTextViewText(R.id.songname_text, runningSong.getTitle() + (player != null && player.isPlaying() ? " is playing" : " is paused"));
        notificationView.setViewVisibility(R.id.not_stop, (player != null && player.isPlaying()) ? View.GONE : View.VISIBLE);
        notificationView.setImageViewResource(R.id.not_play, (player != null && player.isPlaying()) ? R.drawable.pause_icon : R.drawable.play_icon);

        Intent intent = new Intent(MyApp.getAppContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyApp.getAppContext(), 0, intent, 0);


        not = builder
                .setSmallIcon(R.drawable.button_songbar_pause)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
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

    private void modifyNotification() {
        Log.e("SERVICE","call mod");
        if(notificationView==null)
            return;
        notificationView.setTextViewText(R.id.songname_text, runningSong.getTitle() + (player != null && player.isPlaying() ? " is playing" : " is paused"));
        notificationView.setViewVisibility(R.id.not_stop, (player != null && player.isPlaying()) ? View.GONE : View.VISIBLE);
        notificationView.setImageViewResource(R.id.not_play, (player != null && player.isPlaying()) ? R.drawable.pause_icon : R.drawable.play_icon);


        not.contentView = notificationView;


        mNotificationManager.notify(NOTIFY_ID, not);
    }

    private void saveSongStatus(int id, String action, int status) {
        DbManager db = new DbManager(MyApp.getAppContext());
        MoSongCount s = new MoSongCount();
        s.setSongId(id);
        s.setAction(action);
        s.setStatus(status);
        s.setLastMod(CommonFunc.getCurrentTime());
        db.addSongCount(s);
    }

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }

    }


}