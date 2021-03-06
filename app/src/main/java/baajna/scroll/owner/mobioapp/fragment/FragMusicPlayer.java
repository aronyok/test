package baajna.scroll.owner.mobioapp.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mobioapp.baajna.R;

import java.util.ArrayList;

import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.adapter.AdMusicPlayer;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.interfaces.IMusic;
import baajna.scroll.owner.mobioapp.interfaces.OnUpdateUI;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.services.MusicService;
import baajna.scroll.owner.mobioapp.utils.Globals;
import baajna.scroll.owner.mobioapp.utils.SimpleDividerItemDecoration;
import baajna.scroll.owner.mobioapp.utils.TimeConverter;

/**
 * Created by Jewel on 1/24/2016.
 */
public class FragMusicPlayer extends Fragment implements View.OnClickListener, OnUpdateUI, IMusic {
    private static FragMyPlaylist instance;
    public LinearLayout layLoop, layPrev, layPlay, layNext, layShuffle;
    public ImageView imgPlay, imgNext, imgPrev, imgShuffle, imgLoop;
    public TextView tvTitle, tvCTime, tvTTime;
    public SeekBar seekBar;
    int count;
    private View view;
    private ArrayList<MoSong> songList;
    private RecyclerView recyclerView;
    private AdView mAdView;
    private DbManager myDb;
    private AdMusicPlayer adapter;
    //private Handler handler;
    private boolean isShuffle = false;
    private boolean isRepeat = false, isEditable;
    private int currentSong;
    private MoSong runningSong;

    //public static boolean isPaused=true;

    private int runningSongId;
    private MusicService musicService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MyBinder) service).getService();
            setListner();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    public static FragMyPlaylist getInstance() {
        FragMyPlaylist fragMyPlaylist = new FragMyPlaylist();
        return fragMyPlaylist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lay_music_player, container, false);
        init();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent(getContext(), MusicService.class);
        getContext().startService(intent);
        getContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        prepareList();
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unbindService(serviceConnection);

    }

    private void setListner() {
        musicService.setUpdateInterface(this);
    }

    private void init() {
        //retrieve list view


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.addItemDecoration(new SpacesItemDecoration(30));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        adapter = new AdMusicPlayer(getContext()) {
            @Override
            public void onClickItem(View view, int position) {
                musicService.setSong(1);
                musicService.playPause();
            }
        };
        recyclerView.setAdapter(adapter);


        //instantiate list
        songList = new ArrayList<MoSong>();

        tvTitle = (TextView) view.findViewById(R.id.tv_player_title);
        tvCTime = (TextView) view.findViewById(R.id.tv_player_ctime);
        tvTTime = (TextView) view.findViewById(R.id.tv_player_ttime);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);

        layLoop = (LinearLayout) view.findViewById(R.id.lay_player_loop);
        layPrev = (LinearLayout) view.findViewById(R.id.lay_player_prev);
        layPlay = (LinearLayout) view.findViewById(R.id.lay_player_play);
        layNext = (LinearLayout) view.findViewById(R.id.lay_player_next);
        layShuffle = (LinearLayout) view.findViewById(R.id.lay_player_shuffle);

        imgPlay = (ImageView) view.findViewById(R.id.img_player_play);
        imgPrev = (ImageView) view.findViewById(R.id.img_player_prev);
        imgNext = (ImageView) view.findViewById(R.id.img_player_next);
        imgShuffle = (ImageView) view.findViewById(R.id.img_player_shuffle);
        imgLoop = (ImageView) view.findViewById(R.id.img_player_loop);

        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

        imgPlay.setOnClickListener(this);
        imgPrev.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        imgShuffle.setOnClickListener(this);
        imgLoop.setOnClickListener(this);

        layPlay.setOnClickListener(this);
        layPrev.setOnClickListener(this);
        layNext.setOnClickListener(this);
        layShuffle.setOnClickListener(this);
        layLoop.setOnClickListener(this);


        seekBar.setClickable(false);


        prepareList();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    musicService.seek(progress);
                seekBar.getProgress();
                //Log.d("Sajal","Progress"+seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ((MainActivity) getActivity()).setOnUpdateUI(this);
    }

    private void updateMusicPlayer() {
        Log.e("Jewel", "update call");


        if (MusicService.playerState == MusicService.STATE_PLAYING && MusicService.player != null) {

            tvTTime.setText(TimeConverter.getTime(musicService.player.getDuration()));
            tvCTime.setText(TimeConverter.getTime(MusicService.player.getCurrentPosition()));

            seekBar.setMax(MusicService.player.getDuration());
            seekBar.setProgress(MusicService.player.getCurrentPosition());

            imgPlay.setEnabled(true);
            imgPlay.setBackgroundResource(R.drawable.pause_icon);

            if ((seekBar.getProgress() >= ((seekBar.getMax()) * 70) / 100)) {
                count++;

            }
        } else if (MusicService.playerState == MusicService.STATE_NOT_READY) {
            Log.d("Jewel", " not ready");
            imgPlay.setBackgroundResource(R.drawable.loading);
            imgPlay.setEnabled(false);

        } else if (MusicService.playerState == MusicService.STATE_PAUSE) {
            Log.e("Jewel", " pause");

            imgPlay.setBackgroundResource(R.drawable.play_icon);
            imgPlay.setEnabled(true);

        } else if (MusicService.playerState == MusicService.STATE_STOP) {
            Log.d("Jewel", " Stop");
            imgPlay.setBackgroundResource(R.drawable.play_icon);
            imgPlay.setEnabled(true);

        }


    }


    @Override
    public void onClick(View v) {
        Intent startIntent;
        switch (v.getId()) {
            case R.id.img_player_play:
                startIntent = new Intent(getActivity(), MusicService.class);
                startIntent.setAction(MusicService.PLAY_ACTION);
                getActivity().startService(startIntent);


                break;
            case R.id.img_player_next:
                startIntent = new Intent(getActivity(), MusicService.class);
                startIntent.setAction(MusicService.NEXT_ACTION);
                getActivity().startService(startIntent);
                break;
            case R.id.img_player_prev:
                startIntent = new Intent(getActivity(), MusicService.class);
                startIntent.setAction(MusicService.PREV_ACTION);
                getActivity().startService(startIntent);
                break;
            case R.id.img_player_shuffle:
                if (musicService.isRunning)
                    musicService.setShuffle();

                break;

        }
    }


    private void prepareList() {
        //get songs from device
        getSongList();

        //create and set adapter

        adapter.setSongs(songList, false);

        //start music service
        //startMusicService();
    }


    //method to retrieve song info from device
    public void getSongList() {

        myDb = new DbManager(getActivity());
        songList = myDb.getSongs(DbManager.SQL_SONGS_PLAYLIST_RUNNING);
        myDb.close();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void updateUI(final View imgEdit, final View btnSave, final View btnCancel) {
        getSongList();
        adapter.setSongs(songList, false);
        recyclerView.setAdapter(adapter);
        imgEdit.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           imgEdit.setVisibility(View.GONE);
                                           btnSave.setVisibility(View.VISIBLE);
                                           btnCancel.setVisibility(View.VISIBLE);
                                           adapter.setSongs(songList, true);
                                           recyclerView.setAdapter(adapter);
                                       }


                                   }

        );
        btnCancel.setOnClickListener(new View.OnClickListener()

                                     {
                                         @Override
                                         public void onClick(View v) {
                                             getSongList();
                                             adapter.setSongs(songList, false);
                                             recyclerView.setAdapter(adapter);
                                             btnCancel.setVisibility(View.GONE);
                                             btnSave.setVisibility(View.GONE);
                                             imgEdit.setVisibility(View.VISIBLE);
                                             Globals.deletedSongIds = new ArrayList<Integer>();
                                         }
                                     }

        );
        btnSave.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View v) {
                                           adapter.setSongs(songList, false);
                                           recyclerView.setAdapter(adapter);
                                           btnCancel.setVisibility(View.GONE);
                                           btnSave.setVisibility(View.GONE);
                                           imgEdit.setVisibility(View.VISIBLE);
                                           Log.d("Jewel", Globals.deletedSongIds.size() + " si");
                                           DbManager db = new DbManager(getContext());
                                           for (int id : Globals.deletedSongIds)
                                               db.removeFromPlaylist(id, 0);
                                           db.close();
                                       }
                                   }

        );
        if (MusicService.songPosn != currentSong) {
            currentSong = MusicService.songPosn;
            prepareList();
        }
    }

    @Override
    public void onUpdate(MediaPlayer mediaPlayer, MoSong runningSong) {
        this.runningSong = runningSong;
        updateMusicPlayer();
        try {
            ((MainActivity) getContext()).prepareBottomPlayer(runningSong);
        } catch (Exception e) {

        }
    }
}