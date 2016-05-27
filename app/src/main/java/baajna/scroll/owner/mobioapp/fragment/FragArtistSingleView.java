package baajna.scroll.owner.mobioapp.fragment;

/**
 * Created by Sajal
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.utils.MyApp;
import baajna.scroll.owner.mobioapp.utils.Urls;
import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.activity.SingleSongActivity;
import baajna.scroll.owner.mobioapp.connection.InternetConnectivity;
import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;
import baajna.scroll.owner.mobioapp.datamodel.MoArtist;
import baajna.scroll.owner.mobioapp.datamodel.MoGenres;
import baajna.scroll.owner.mobioapp.datamodel.MoPlayList;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;
import baajna.scroll.owner.mobioapp.services.MusicService;
import baajna.scroll.owner.mobioapp.utils.CommonFunc;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FragArtistSingleView extends Fragment {
    private static final String ARTIST = "artist_id";
    private static final String ALBUM = "album_id";
    private static FragArtistSingleView fragArtistSingleView;
    MoArtist moArtist;

    LinearLayout lay_Album_Top;
    LinearLayout lay_Song_Top;
    ImageView viewAll;
    public int artistId, albumId;
    private LinearLayout mainLay, layTop, layTrend, layMoods, layMidle;
    private RecyclerView recyclerView;
    private ImageView imgArtistCover;
    private Context context;
    private View view;
    private ArrayList<MoArtist> artistTop, artistTrend;
    private ArrayList<MoAlbum> albumTop;
    private ArrayList<MoSong> songTop;
    private ArrayList<MoGenres> genres;
    private LayoutInflater inflater;
    private AdView mAdView;
    private FrameLayout rowHome2;
    private MusicService musicService;

    public static FragArtistSingleView getInstance(int artistId, int albumId) {

        fragArtistSingleView = new FragArtistSingleView();
        Bundle bundle = new Bundle();
        bundle.putInt(ARTIST, artistId);
        bundle.putInt(ALBUM, albumId);
        fragArtistSingleView.setArguments(bundle);
        return fragArtistSingleView;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lay_single_artist, container, false);
        initViews();
        if (InternetConnectivity.isConnectedToInternet(getContext())) {
            loadData();
        } else
        {
            prepareDisplay();
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void initViews() {
        musicService=((MainActivity)getContext()).musicService;
        context = getActivity();
        inflater = LayoutInflater.from(context);

        artistId = getArguments().getInt(ARTIST);
        Log.e("Sajal", "artist ID : " + artistId);
        albumId = getArguments().getInt(ALBUM);
        albumTop = new ArrayList<>();
        songTop = new ArrayList<>();
        artistTrend = new ArrayList<>();
        //albumsMoods = new ArrayList<>();

        mainLay = (LinearLayout) view.findViewById(R.id.layMain);
        imgArtistCover = (ImageView) view.findViewById(R.id.img_artist_view);

        lay_Album_Top= (LinearLayout) view.findViewById(R.id.lay_album_more);
        lay_Song_Top= (LinearLayout) view.findViewById(R.id.lay_more);
        viewAll = (ImageView) view.findViewById(R.id.img_more);

        rowHome2=(FrameLayout)view.findViewById(R.id.row_albumhome2);

        layTop = new LinearLayout(getContext());
        layTop.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layTop.setOrientation(LinearLayout.VERTICAL);

        layMidle = new LinearLayout(getContext());
        layMidle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layMidle.setOrientation(LinearLayout.VERTICAL);

        layTrend = new LinearLayout(getContext());
        layTrend.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layTrend.setOrientation(LinearLayout.VERTICAL);

        mainLay.addView(layMidle);
        mainLay.addView(layTop);

        mainLay.addView(layTrend);

        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);


    }



    private void loadData() {


        if (InternetConnectivity.isConnectedToInternet(getActivity())) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("artist_id", artistId);

            client.post("http://52.89.156.64/index.php/api/artist_top_album", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (CommunicationLayer.parseTopAlbumArtistData(response) == 1) {
                        if (InternetConnectivity.isConnectedToInternet(MyApp.getAppContext())) {
                            AsyncHttpClient client = new AsyncHttpClient();
                            RequestParams params = new RequestParams();
                            params.put("artist_id", artistId);
                            client.post("http://52.89.156.64/index.php/api/artist_top_songs", params, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    if (CommunicationLayer.parseTopSongArtistData(response) == 1) {
                                       prepareDisplay();
                                    }
                                }


                            });

                        }
                    }
                }
            });

        }



    }

    public void prepareDisplay() {
        moArtist = new MoArtist();
        DbManager db = DbManager.getInstance();
        moArtist = db.getArtist(artistId);
        albumTop = db.getTopAlbums(artistId);
        songTop = db.getTopSongsByArtist(artistId);
        db.close();

        if (albumTop.size() > 0) {

            layTop.addView(prepareTextView("TOP ALBUMS"));
            int lenAlbums = albumTop.size();
            if (lenAlbums > 10) {
                lenAlbums = 10;
            }
            for (int i = 0; i < lenAlbums-1; i = i + 2) {
                MoAlbum moAlbum1 = new MoAlbum();
                moAlbum1.setId1(albumTop.get(i).getId());
                moAlbum1.setTitle1(albumTop.get(i).getTitle());
                moAlbum1.setImgUrl1(albumTop.get(i).getImgUrl());

                if ((i + 1) < lenAlbums) {
                    moAlbum1.setId2(albumTop.get(i + 1).getId());
                    moAlbum1.setTitle2(albumTop.get(i + 1).getTitle());
                    moAlbum1.setImgUrl2(albumTop.get(i + 1).getImgUrl());
                }
                else {
                   //FrameLayout frameLayout2 = (FrameLayout) view.findViewById(R.id.row_albumhome2);
                    //frameLayout2.setVisibility(view.GONE);

                }
                layTop.addView(prepareViewArtistTopAlbums(moAlbum1));
            }

            //layTop.addView(prepareViewArtistTopAlbums(moAlbum2));
            //layTop.addView(prepareViewArtist(moAlbum3));
            layTop.addView(prepareTextViewInside());
            layTop.addView(prepareMoreViewArtistAlbums());
            layTop.addView(prepareTextViewInside());
            layTop.addView(prepareTextView("TOP SONGS"));
        }


        if (songTop.size() > 0) {

            int lenSongs = songTop.size();
            if (lenSongs > 3) {
                lenSongs = 3;
            }

            for (int i = 0; i < lenSongs; i++) {
                MoSong moSong1 = new MoSong();
                moSong1.setId(songTop.get(i).getId());
                moSong1.setTitle(songTop.get(i).getTitle());
                moSong1.setArtist_name(songTop.get(i).getArtist_name());
                moSong1.setImgUrl(songTop.get(i).getImgUrl());


                layTrend.addView(prepareViewArtistTopSongs(moSong1));
            }

            layTrend.addView(prepareTextViewInside());

        }

        String imgUrl = TextUtils.isEmpty(moArtist.getImge()) ? Urls.BASE_URL + Urls.IMG_ARTIST + "6e83e5d5fee89ad93c147322a1314076.jpg" :Urls.BASE_URL + Urls.IMG_ARTIST +moArtist.getImge();
        Log.e("SAJAL","img:"+imgUrl);
        Picasso.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.main_myplaylist)
                .into(imgArtistCover);



        lay_Song_Top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //((MainActivity)context).replaceFrag(FragExpandable.getInstance(album.getId(),1),album.getTitle());
                ((MainActivity) context).replaceFrag(FragExpandableListArtist.getInstance(artistId, 1), moArtist.getName());
                /*((MainActivity) context).replaceFrag(FragAlbumArtist.getInstance(), "Albums");*/
                Log.d("Sajal", "artistID-view all " + moArtist.getId());
                Log.d("Sajal", "artistIdall" + moArtist.getArtistDetails());
            }
        });

    }

    private TextView prepareTextView(String text) {
        TextView tv = new TextView(context);
        tv.setText(text);
        //tv.getFontFeatureSettings();
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(11);
        tv.setTextColor(Color.parseColor("#808080"));
        tv.setGravity(Gravity.CENTER);
        //tv.setTextColor(context.getResources().getColor(R.color.app_bar));
        //tv.setBackgroundColor(context.getResources().getColor(R.color.white));
        tv.setBackgroundColor(Color.parseColor("#E8E8E8"));
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setPadding(CommonFunc.getPixel(context, 10), CommonFunc.getPixel(context, 10), 0, CommonFunc.getPixel(context, 10));
        return tv;
    }

    private TextView prepareTextViewblank(String text) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextSize(10);
        tv.setTextColor(context.getResources().getColor(R.color.white));
        tv.setBackgroundColor(context.getResources().getColor(R.color.white));
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return tv;
    }

    private TextView prepareTextViewInside() {
        TextView tv = new TextView(context);
        //tv.setBackgroundColor(context.getResources().getColor(R.color.app_bar));
        tv.setBackgroundColor(Color.parseColor("#dfdfdf"));
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
        return tv;
    }

    private LinearLayout prepareMoreViewArtistAlbums() {
        LinearLayout laymore = (LinearLayout) inflater.inflate(R.layout.row_more, null);
        //laymore.setPadding(0, 10, 0, 10);
        TextView textViewMore = (TextView) laymore.findViewById(R.id.tv_more);
        ImageView imageViewMore = (ImageView) laymore.findViewById(R.id.img_more);
        textViewMore.setTextSize(13);
        textViewMore.setTypeface(Typeface.DEFAULT_BOLD);
        textViewMore.setTextColor(Color.parseColor("#303030"));
        laymore.setBackgroundResource(R.drawable.btn_press);

        textViewMore.setText("All Albums");
        imageViewMore.setImageResource(R.drawable.more_icon);
        laymore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).replaceFrag(FragArtistAlbums.getInstance(moArtist.getId()), moArtist.getName());
                /*((MainActivity) context).replaceFrag(FragAlbumArtist.getInstance(), "Albums");*/
                Log.d("Sajal", "2 clicked" + artistId);
                Log.d("Sajal", "artistID-more " + moArtist.getId());
                Log.d("Sajal", "artistId_all" + moArtist.getName());
            }
        });
        return laymore;

    }


    private LinearLayout prepareViewArtistTopAlbums(final MoAlbum moAlbum) {

        LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.row_artist_top_trend, null);
        lay.setPadding(0, 10, 0, 10);
        FrameLayout frameLayout1 = (FrameLayout) lay.findViewById(R.id.row_albumhome1);
        FrameLayout frameLayout2 = (FrameLayout) lay.findViewById(R.id.row_albumhome2);

        TextView textView1 = (TextView) lay.findViewById(R.id.tv_new_release_title1);
        TextView textView2 = (TextView) lay.findViewById(R.id.tv_new_release_title2);
        textView1.setText(moAlbum.getTitle1());
        textView2.setText(moAlbum.getTitle2());
        frameLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((MainActivity) context).replaceFrag(FragExpandable.getInstance(moAlbum.getId1(), 1), moAlbum.getTitle1());
                Log.d("Sajal", "1 clicked");
                Log.d("Sajal", "album on disk1" + moAlbum.getId1());
            }
        });
        frameLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) context).replaceFrag(FragExpandable.getInstance(moAlbum.getId2(), 1), moAlbum.getTitle2());
                Log.d("Sajal", "2 clicked");
                Log.d("Sajal", "album on disk2" + moAlbum.getId2());
            }
        });
        ImageView imageView1 = (ImageView) lay.findViewById(R.id.img_new_albums1);
        ImageView imageView2 = (ImageView) lay.findViewById(R.id.img_new_albums2);

        String imgUrl1 = TextUtils.isEmpty(moAlbum.getImgUrl1()) ? Urls.BASE_URL + Urls.IMG_ALBUM + "6e83e5d5fee89ad93c147322a1314076.jpg" : Urls.BASE_URL + Urls.IMG_ALBUM + moAlbum.getImgUrl1();
        //String imgUrl= moArtist.getImge().isEmpty()?Urls.BASE_URL+Urls.IMG_ALBUM +"6e83e5d5fee89ad93c147322a1314076.jpg":/*Urls.BASE_URL+Urls.IMG_ALBUM +*/artist.getImge();
        Log.d("Sajal", "foundUrl" + imgUrl1);
        Picasso.with(context)
                .load(imgUrl1)
                .placeholder(R.drawable.main_myplaylist)
                .into(imageView1);
        String imgUrl2 = TextUtils.isEmpty(moAlbum.getImgUrl2()) ? Urls.BASE_URL + Urls.IMG_ALBUM + "6e83e5d5fee89ad93c147322a1314076.jpg" : Urls.BASE_URL + Urls.IMG_ALBUM + moAlbum.getImgUrl2();
        Log.d("Sajal", "foundUrl" + imgUrl1);
        Picasso.with(context)
                .load(imgUrl2)
                .placeholder(R.drawable.main_myplaylist)
                .into(imageView2);

        return lay;


    }

    private LinearLayout prepareViewArtistTopSongs(final MoSong moSong) {

        LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.row_song_list_artist, null);
        lay.setPadding(0, 10, 0, 10);
        ImageView img = (ImageView) lay.findViewById(R.id.img_song_album_cover);
        LinearLayout lay_Song_Detail= (LinearLayout) lay.findViewById(R.id.lay_song_detail);
        ImageView imgOptions = (ImageView) lay.findViewById(R.id.img_song_detail_view_icon);
        LinearLayout laySong = (LinearLayout) lay.findViewById(R.id.lay_song_id_artist);
        //FrameLayout frameLayout2 = (FrameLayout) lay.findViewById(R.id.row_albumhome2);

        TextView textView1 = (TextView) lay.findViewById(R.id.tv_song_title);
        TextView textView2 = (TextView) lay.findViewById(R.id.tv_song_album);
        textView1.setText(moSong.getTitle());
        textView2.setText(moSong.getArtist_name());
        laySong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MoGenres album=albumList.get(getAdapterPosition());

                DbManager mydb = new DbManager(context);
                MoPlayList playList = new MoPlayList();
                playList.setId(0);


                playList.setSongId(moSong.getId());
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
                    musicService.playSong(MusicService.songPosn);
                } else {
                    /*Intent playIntent = new Intent(_context, MusicPlayerActivity.class);
                    playIntent.putExtra("ParentClassName", "SongListActivity");
                    playIntent.putExtra("song_index", groupPosition);
                    //Globals.songNumber = selectedPos;
                    _context.startActivity(playIntent);*/


                    startMusicService();
                }
            }
        });

        lay_Song_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoSong song = new MoSong();
                //((MainActivity)_context).replaceFrag(FragSingleSong.getInstance(song.getId()), song.getTitle());

                context.startActivity(new Intent(context, SingleSongActivity.class).putExtra("song_id", moSong.getId()));
                Log.d("Sajal", "2 clicked" + moSong.getId());
            }
        });

        String imgUrl1 = TextUtils.isEmpty(moSong.getImgUrl()) ? Urls.BASE_URL + Urls.IMG_SONG + "6e83e5d5fee89ad93c147322a1314076.jpg" : Urls.BASE_URL + Urls.IMG_SONG + moSong.getImgUrl();
        //String imgUrl= moArtist.getImge().isEmpty()?Urls.BASE_URL+Urls.IMG_ALBUM +"6e83e5d5fee89ad93c147322a1314076.jpg":/*Urls.BASE_URL+Urls.IMG_ALBUM +*/artist.getImge();
        Log.d("Sajal", "foundUrl" + imgUrl1);
        Picasso.with(context)
                .load(imgUrl1)
                .placeholder(R.drawable.main_myplaylist)
                .into(img);

        return lay;


    }
    private void startMusicService() {
        Intent startIntent = new Intent(context, MusicService.class);
        startIntent.setAction(MusicService.MAIN_ACTION);
        context.startService(startIntent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {


            case android.R.id.home:

                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();


                return true;


            case R.id.action_end_page:


                return true;

            default:

                return super.onOptionsItemSelected(item);


        }


    }


}