package baajna.scroll.owner.mobioapp.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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

import baajna.scroll.owner.mobioapp.adapter.AdMusicPlayer;
import baajna.scroll.owner.mobioapp.interfaces.OnUpdateUI;
import baajna.scroll.owner.mobioapp.utils.Globals;
import baajna.scroll.owner.mobioapp.utils.SimpleDividerItemDecoration;
import baajna.scroll.owner.mobioapp.utils.TimeConverter;
import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.interfaces.IMusic;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.services.MusicService;

import java.util.ArrayList;

/**
 * Created by Jewel on 1/24/2016.
 */
public class FragMusicPlayer extends Fragment implements View.OnClickListener, OnUpdateUI, IMusic {
    private static FragMyPlaylist instance;
    public LinearLayout layLoop,layPrev,layPlay,layNext,layShuffle;
    public ImageView imgPlay, imgNext, imgPrev, imgShuffle, imgLoop;
    public TextView tvTitle, tvCTime, tvTTime;
    int count;

    public SeekBar seekBar;
    private View view;
    private ArrayList<MoSong> songList;
    private RecyclerView recyclerView;
    private AdView mAdView;
    private DbManager myDb;
    private AdMusicPlayer adapter;
    private Handler handler;
    private boolean isShuffle = false;
    private boolean isRepeat = false, isEditable;
    private int currentSong;
    private MoSong runningSong;

    public static FragMyPlaylist getInstance() {
        FragMyPlaylist fragMyPlaylist = new FragMyPlaylist();
        return fragMyPlaylist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lay_music_player, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }


    private void init() {
        //retrieve list view
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.addItemDecoration(new SpacesItemDecoration(30));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        adapter = new AdMusicPlayer(getContext());
        recyclerView.setAdapter(adapter);
        //instantiate list
        songList = new ArrayList<MoSong>();

        tvTitle = (TextView) view.findViewById(R.id.tv_player_title);
        tvCTime = (TextView) view.findViewById(R.id.tv_player_ctime);
        tvTTime = (TextView) view.findViewById(R.id.tv_player_ttime);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);

        layLoop= (LinearLayout) view.findViewById(R.id.lay_player_loop);
        layPrev= (LinearLayout) view.findViewById(R.id.lay_player_prev);
        layPlay= (LinearLayout) view.findViewById(R.id.lay_player_play);
        layNext= (LinearLayout) view.findViewById(R.id.lay_player_next);
        layShuffle= (LinearLayout) view.findViewById(R.id.lay_player_shuffle);

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

        MusicService.setUpdateInterface(this);


        seekBar.setClickable(false);

        startMusicService();
        prepareList();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    MusicService.seek(progress);
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

        if (handler == null)
            handler = new Handler();
        playerTimeUpdater();

    }

    private void playerTimeUpdater() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MusicService.playerState == MusicService.STATE_PLAYING && MusicService.player != null)
                    //tvCTime.setText(TimeConverter.getTime(MusicService.player.getCurrentPosition()));
                    updateMusicPlayer();
                playerTimeUpdater();
            }
        }, 1000);
    }

    private void updateMusicPlayer() {
        if (MusicService.playerState == MusicService.STATE_PLAYING && MusicService.player != null) {
            //if (MusicService.playerState == MusicService.STATE_PLAYING) {
            tvTitle.setText(MusicService.getRunningSongTitle());
            tvCTime.setText(TimeConverter.getTime(MusicService.player.getCurrentPosition()));
            tvTTime.setText(TimeConverter.getTime(MusicService.player.getDuration()));
            imgPlay.setEnabled(true);

            //imgPlay.setBackgroundResource(MusicService.player.isPlaying() ? R.drawable.ico_pause : R.drawable.ico_play);
            imgPlay.setBackgroundResource(R.drawable.pause_icon);
            imgShuffle.setImageResource(MusicService.shuffle ? R.drawable.shuffle_icon_inactive : R.drawable.shuffle_icon);
            seekBar.setMax(MusicService.player.getDuration());
            seekBar.setProgress(MusicService.player.getCurrentPosition());
            if((seekBar.getProgress() >= ((seekBar.getMax())*70)/100)){
                count++;
                Log.d("Sajal","PlayProgress " +count);
            }
        } else if (MusicService.playerState == MusicService.STATE_NOT_READY) {
            Log.d("Jewel", " not ready");
            imgPlay.setBackgroundResource(R.drawable.loading);
            imgPlay.setEnabled(false);
        } else if (MusicService.playerState == MusicService.STATE_PAUSE) {
            Log.d("Jewel", " pause");
            imgPlay.setBackgroundResource(R.drawable.play_icon);
        } else if (MusicService.playerState == MusicService.STATE_STOP) {
            Log.d("Jewel", " Stop");
            imgPlay.setBackgroundResource(R.drawable.play_icon);
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_player_play:
                if (MusicService.playerState != MusicService.STATE_NOT_READY) {
                    MusicService.playPause();

                } else {
                    startMusicService();
                }


                break;
            case R.id.img_player_next:
                if (MusicService.isRunning)
                    MusicService.playNext();
                else
                    startMusicService();

                break;
            case R.id.img_player_prev:
                if (MusicService.isRunning)
                    MusicService.playPrev();
                else
                    startMusicService();

                break;
            case R.id.img_player_shuffle:
                if (MusicService.isRunning)
                    MusicService.setShuffle();
                else
                    startMusicService();
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


    private void startMusicService() {
        Intent startIntent = new Intent(getActivity(), MusicService.class);
        startIntent.setAction(MusicService.NORMAL_ACTION);
        getActivity().startService(startIntent);
    }

    //method to retrieve song info from device
    public void getSongList() {

        myDb = new DbManager(getActivity());
        songList = myDb.getSongs(DbManager.SQL_SONGS_PLAYLIST_RUNNING);
        myDb.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (handler == null)
            handler = new Handler();
        prepareList();


    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
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
    public void onUpdate(MediaPlayer mediaPlayer,MoSong runningSong) {
        this.runningSong=runningSong;
        updateMusicPlayer();
        try {
            ((MainActivity) getContext()).prepareBottomPlayer();
        } catch (Exception e) {

        }
    }
}