package baajna.scroll.owner.mobioapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import baajna.scroll.owner.mobioapp.adapter.AdPopupList;
import baajna.scroll.owner.mobioapp.connection.InternetConnectivity;
import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;
import baajna.scroll.owner.mobioapp.datamodel.MoPlayList;
import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;
import baajna.scroll.owner.mobioapp.utils.AlphaForeGroundColorSpan;
import baajna.scroll.owner.mobioapp.utils.MyApp;
import baajna.scroll.owner.mobioapp.utils.ScrollViewHelper;
import baajna.scroll.owner.mobioapp.utils.Urls;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.services.MusicService;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import com.mobioapp.baajna.R;

/**
 * Created by Sajal on 1/17/2016.
 */
public class SingleSongActivity extends AppCompatActivity {

    //private View view = null;
    public static int song_id;
    MoAlbum album;
    MoSong song;
    private PackageInfo pInfo = null;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private LinearLayout laySingleSongView;
    private LinearLayout laySingleSongOptions;
    private LinearLayout laySingleSongAddToQueue;
    private LinearLayout laySingleSongAddToPlaylist;
    private LinearLayout laySingleSongDownload;
    private LinearLayout laySingleSongLike;
    private TextView tvSingleSongAlbumName;
    private TextView tvSingleSongArtistName;
    private TextView tvSingleSongComposerName;
    private TextView tvSingleSongDescription;
    private TextView tvSingleSongArtists;
    private ImageView imgSingleSongCover;
    private ImageView imgSingleSongPlay;
    private ArrayList<MoAlbum> albums;
    private ArrayList<MoSong> songs;
    /**
     * Alpha Toolbar
     **/
    private AlphaForeGroundColorSpan mAlphaForegroundColorSpan;
    private SpannableString mSpannableString;


    private Context context;

    /*public static SingleSongActivity getInstance(int song_id) {
        SingleSongActivity fragSingleSong = new SingleSongActivity();
        //bundle.putInt("song_id", song_id);
        //fragSingleSong.setArguments(bundle);
        return fragSingleSong;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_single_song_view);

        initialize();
        prepareDis();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // NavUtils.navigateUpFromSameTask(this);
            this.finish();

        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        context = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("song_id")) {
            song_id = bundle.getInt("song_id");
        }

        getDBData();
        Log.d("Sajal", "song id:" + song_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int colors[] = {0xff0099CC, 0xff32CBCC};
        final GradientDrawable cd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);

        String title = song.getTitle();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(cd);

        cd.setAlpha(0);

        ScrollViewHelper scrollViewHelper = (ScrollViewHelper) findViewById(R.id.scrollViewHelper);
        scrollViewHelper.setOnScrollViewListener(new ScrollViewHelper.OnScrollViewListener() {

            @Override
            public void onScrollChanged(ScrollViewHelper v, int l, int t, int oldl, int oldt) {
                setTitleAlpha(255 - getAlphaforActionBar(v.getScrollY()));
                cd.setAlpha(getAlphaforActionBar(v.getScrollY()));
            }

            private int getAlphaforActionBar(int scrollY) {
                int minDist = 0, maxDist = 550;
                if (scrollY > maxDist) {

                    return 255;
                } else {
                    if (scrollY < minDist) {
                        return 0;
                    } else {
                        return (int) ((255.0 / maxDist) * scrollY);
                    }
                }
            }


        });

        mSpannableString = new SpannableString(title);
        mAlphaForegroundColorSpan = new AlphaForeGroundColorSpan(0xFFFFFF);


        //about_version = (TextView) findViewById(R.id.txt_app_version_name);
        laySingleSongView = (LinearLayout) findViewById(R.id.lay_single_song_view);
        laySingleSongOptions = (LinearLayout) findViewById(R.id.lay_single_song_options);
        laySingleSongAddToQueue = (LinearLayout) findViewById(R.id.lay_single_song_add_to_queue);
        laySingleSongAddToPlaylist = (LinearLayout) findViewById(R.id.lay_single_song_add_to_playlist);
        laySingleSongDownload = (LinearLayout) findViewById(R.id.lay_single_song_download);
        laySingleSongLike = (LinearLayout) findViewById(R.id.lay_single_song_star);

        tvSingleSongAlbumName = (TextView) findViewById(R.id.tv_single_song_album);
        tvSingleSongArtistName = (TextView) findViewById(R.id.tv_single_song_artist);
        //tvSingleSongComposerName = (TextView) findViewById(R.id.tv_single_song_composer);
        tvSingleSongDescription = (TextView) findViewById(R.id.tv_test_description);
        tvSingleSongArtists = (TextView) findViewById(R.id.tv_single_song_artists);

        imgSingleSongCover = (ImageView) findViewById(R.id.img_single_song_cover);
        imgSingleSongPlay = (ImageView) findViewById(R.id.img_single_song_play);

        //song_id = getArguments().getInt("song_id");
        Log.d("Sajal", song_id + " album");
    }

    private void setTitleAlpha(float alpha) {
        if (alpha < 1) {
            alpha = 1;
        }
        mAlphaForegroundColorSpan.setAlpha(alpha);
        mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(mSpannableString);
    }

    private void getDBData(){
        song = new MoSong();
        DbManager myDb = new DbManager(this);
        song = myDb.getSong(song_id);
        myDb.close();
    }
    private void prepareDis() {
        final int position;
        getDBData();
        tvSingleSongAlbumName.setText(song.getTitle());
        tvSingleSongArtistName.setText(song.getArtist_name());
        //tvSingleSongComposerName.setText(" ");
        tvSingleSongDescription.setText(song.getSongDetails());
        tvSingleSongArtists.setText(" ");

        String imgUrl = song.getImgUrl().isEmpty() ? Urls.BASE_URL + Urls.IMG_SONG + "6e83e5d5fee89ad93c147322a1314076.jpg" :Urls.BASE_URL+Urls.IMG_SONG +song.getImgUrl();
        Picasso.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.sync_icon)
                .into(imgSingleSongCover);


        imgSingleSongPlay.setBackgroundResource(R.drawable.img_btn_play);


        imgSingleSongPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (v == imgSingleSongPlay) {
                    imgSingleSongPlay.setBackgroundResource(MusicService.player.isPlaying() ? R.drawable.ico_pause : R.drawable.ico_play);
                }*/
                DbManager mydb = new DbManager(context);
                MoPlayList playList = new MoPlayList();
                playList.setId(song_id);


                playList.setSongId(song_id);
                long result = mydb.addToPlaylist(playList);
                MusicService.songs = mydb.getSongs(DbManager.SQL_SONGS_PLAYLIST_RUNNING);
                MusicService.songPosn = MusicService.songs.size() - 1;
                mydb.close();
                // songs = MusicService.songs;
                for (int i = 0; i < MusicService.songs.size(); i++) {
                    if (playList.getSongId() == MusicService.songs.get(i).getId()) {
                        MusicService.songPosn = i;
                        break;
                    }
                }
                if (MusicService.isRunning) {
                    MusicService.playSong(MusicService.songPosn);
                } else {
                    startMusicService();
                }
            }


        });

        laySingleSongAddToQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DbManager mydb = new DbManager(context);
                MoPlayList playList = new MoPlayList();
                playList.setId(song_id);

                playList.setSongId(song_id);
                long result = mydb.addToPlaylist(playList);
                MusicService.songs = mydb.getSongs(DbManager.SQL_SONGS_PLAYLIST_RUNNING);
                mydb.close();
                Toast.makeText(context, "Added player Queue", Toast.LENGTH_LONG).show();

            }
        });

        laySingleSongAddToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(SingleSongActivity.this);
                dialog.setContentView(R.layout.popup_playlist);
                dialog.setTitle("Add to playlist");

                //dialog.getWindow().setMargin(Margin.BOTTOM);
                //dialog.getWindow().setGravity(Gravity.TOP | Gravity.RIGHT);
                dialog.getWindow().setTitleColor(getResources().getColor(R.color.white));
                dialog.getWindow().setBackgroundDrawableResource(R.color.pop_up_color);


                DbManager db = new DbManager(context);
                final ArrayList<MoPlayList> playList1 = db.getPlayListName();
                db.close();


                RecyclerView listView = (RecyclerView) dialog.findViewById(R.id.recycler_view);
                listView.setLayoutManager(new LinearLayoutManager(context));
                AdPopupList adapter = new AdPopupList(SingleSongActivity.this) {
                    @Override
                    public void onAfterClick(final int position) {
                        int playlistId = playList1.get(position).getId();
                        //final int viewPos=position;
                        //int albumId=albums.get(viewPos).getId();
                        DbManager db = new DbManager(context);
                        //ArrayList<MoSong> songs=db.getSongsByAlbum(playlistId);

                        ArrayList<MoPlayList> playLists = new ArrayList<MoPlayList>();
                        MoPlayList playList;
                        //for(int i=0;i<songs.size();i++){
                        playList = new MoPlayList();
                        playList.setId(playlistId);
                        playList.setSongId(song_id);

                        playLists.add(playList);
                        //}
                        db.addPlayLists(playLists);
                        db.close();
                        Log.d("Jewel", "a " + playLists.size() + ": " + song_id);
                        dialog.dismiss();
                    }
                };
                adapter.setData(playList1);
                //listView.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,data ));
                listView.setAdapter(adapter);


                TextView tvCreate = (TextView) dialog.findViewById(R.id.tv_new_playlist);
                tvCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final Dialog mDialog = new Dialog(SingleSongActivity.this);
                        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        mDialog.setContentView(R.layout.popup_create_playlist);
                        Button btnSave = (Button) mDialog.findViewById(R.id.btn_save);
                        Button btnCancel = (Button) mDialog.findViewById(R.id.btn_close_popup);

                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText editText = (EditText) mDialog.findViewById(R.id.edt_album);
                                String name = editText.getText().toString().trim();
                                if (!name.isEmpty()) {
                                    DbManager db = new DbManager(MyApp.getAppContext());
                                    db.createPlaylist(name);
                                    db.close();
                                    mDialog.dismiss();
                                } else
                                {
                                    Toast.makeText(context, "Empty name !!", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        });
                        mDialog.show();
                    }
                });
                dialog.show();
            }
        });

        laySingleSongDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InternetConnectivity.isConnectedToInternet(context)) {
                    CommunicationLayer.getInstance().getDownloadFile("1", song_id + "");
                    // new DownloadAsyncTask("1").execute("http://192.168.1.145/sparkle/uploads/song_file/4a0d741498b0655405f66d1f42d72e2c.mp3");
                }


            }
        });

        /*setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ActionBar actionBar = getSupportActionBar();
        Spannable text = new SpannableString("Albums");
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1B306E")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_activity);*/

    }

    private void startMusicService() {
        Intent startIntent = new Intent(context, MusicService.class);
        startIntent.setAction(MusicService.MAIN_ACTION);
        context.startService(startIntent);
    }
}

