package baajna.scroll.owner.mobioapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.mobioapp.baajna.BuildConfig;
import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.adapter.HAdAlbum;
import baajna.scroll.owner.mobioapp.adapter.HAdArtist;
import baajna.scroll.owner.mobioapp.connection.InternetConnectivity;
import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;
import baajna.scroll.owner.mobioapp.datamodel.MoArtist;
import baajna.scroll.owner.mobioapp.datamodel.MoGenres;
import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;
import baajna.scroll.owner.mobioapp.utils.CommonFunc;
import baajna.scroll.owner.mobioapp.utils.Globals;
import baajna.scroll.owner.mobioapp.utils.Urls;
import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

/**
 * Created by Jewel on 1/24/2016.
 * Updated by Sajal
 */
public class FragHomePage extends Fragment implements OnQueryTextListener {

    private static FragHomePage fragHomePage;
    private LinearLayout mainLay, layTop, layNewRelease, layMoods, layMiddle, layArtist;
    private HAdAlbum adapter;
    private HAdArtist adArtist;
    private Context context;
    private View view;
    private ProgressBar progressbar;
    private ArrayList<MoAlbum> albumsUp, albumsTop;
    private ArrayList<MoArtist> artistTop;
    private ArrayList<MoGenres> albumsMoods;
    private LayoutInflater inflater;
    private AdView mAdView;

    public static FragHomePage getInstance() {

        fragHomePage = new FragHomePage();
        return fragHomePage;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lay_home, container, false);
        //getVersionInfo();
        initViews();
        if (InternetConnectivity.isConnectedToInternet(getContext())) {
            if (progressbar.getVisibility() != View.VISIBLE)
                progressbar.setVisibility(View.VISIBLE);
            loadData();

        } else {
            prepareDisplay();
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setHasOptionsMenu(true);
    }


    private void initViews() {
        context = getActivity();
        inflater = LayoutInflater.from(context);

        albumsTop = new ArrayList<>();
        albumsUp = new ArrayList<>();
        albumsMoods = new ArrayList<>();
        artistTop = new ArrayList<>();

        progressbar = (ProgressBar) view.findViewById(R.id.progressBar);

        mainLay = (LinearLayout) view.findViewById(R.id.layMain);
        layTop = new LinearLayout(getContext());
        layTop.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layTop.setOrientation(LinearLayout.VERTICAL);
        layNewRelease = new LinearLayout(getContext());
        layNewRelease.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layNewRelease.setOrientation(LinearLayout.VERTICAL);
        layMiddle = new LinearLayout(getContext());
        layMiddle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layMiddle.setOrientation(LinearLayout.VERTICAL);
        layMoods = new LinearLayout(getContext());
        layMoods.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layMoods.setOrientation(LinearLayout.VERTICAL);
        layArtist = new LinearLayout(getContext());
        layArtist.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layArtist.setOrientation(LinearLayout.VERTICAL);


        mainLay.addView(layArtist);
        mainLay.addView(layTop);
        mainLay.addView(layNewRelease);
        mainLay.addView(layMiddle);
        mainLay.addView(layMoods);


        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapter.setFilter(albumsUp);
                        adapter.setFilter(albumsTop);
                        adArtist.setFilter(artistTop);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }


    public boolean onQueryTextChange(String newText) {
        final ArrayList<MoAlbum> filteredModelList = filterUp(albumsUp, newText);
        final ArrayList<MoAlbum> filteredModelList2 = filterTop(albumsTop, newText);
        final ArrayList<MoArtist> filteredModelListArtist = filterArtist(artistTop, newText);
        adapter.setFilter(filteredModelList);
        adapter.setFilter(filteredModelList2);
        adArtist.setFilter(filteredModelListArtist);
        return true;
    }


    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<MoAlbum> filterUp(ArrayList<MoAlbum> albumUp, String query) {
        query = query.toLowerCase();
        DbManager db = new DbManager(getContext());
        albumsUp = db.getAlbums(DbManager.SQL_ALBUMS_TOP);
        db.close();
        adapter.setData(albumsUp);
        final ArrayList<MoAlbum> filteredModelList = new ArrayList<>();
        for (MoAlbum album : albumUp) {
            final String text = album.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(album);
            }
        }
        return filteredModelList;
    }

    private ArrayList<MoAlbum> filterTop(ArrayList<MoAlbum> albumTop, String query) {
        query = query.toLowerCase();
        DbManager db = new DbManager(getContext());
        albumsTop = db.getAlbums(DbManager.SQL_ALBUMS_TOP);
        db.close();
        adapter.setData(albumsTop);

        final ArrayList<MoAlbum> filteredModelList2 = new ArrayList<>();
        for (MoAlbum album : albumTop) {
            final String text = album.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList2.add(album);
            }
        }
        return filteredModelList2;
    }

    private ArrayList<MoArtist> filterArtist(ArrayList<MoArtist> artistsTop, String query) {
        query = query.toLowerCase();
        DbManager db = new DbManager(getContext());
        artistTop = db.getTopArtists(DbManager.SQL_TOP_ARTISTS);
        db.close();
        adArtist.setData(artistTop);

        final ArrayList<MoArtist> filteredModelListArtists = new ArrayList<>();
        for (MoArtist artist : artistsTop) {
            final String text = artist.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelListArtists.add(artist);
            }
        }
        return filteredModelListArtists;
    }

    private void getDBData() {
        DbManager db = new DbManager(getActivity());
        albumsMoods = db.getGenres(DbManager.SQL_GENRES);
        artistTop = db.getTopArtists(DbManager.SQL_TOP_ARTISTS);
        albumsTop = db.getAlbums(DbManager.SQL_TOP_ALBUMS);
        albumsUp = db.getNewAlbums(Globals.releaseDateLimit);
        db.close();
    }

    private void getVersionInfo() {

        TextView textViewVersionInfo = (TextView) view.findViewById(R.id.textview_version_info);
        //Use the following code to get the version info from the Gradle build file.
        String versionName = BuildConfig.VERSION_NAME;
        //int versionCode = BuildConfig.VERSION_CODE;
        textViewVersionInfo.setText(textViewVersionInfo.getText() + String.format("\n version name = %s", versionName));

    }


    private void loadData() {
        if (InternetConnectivity.isConnectedToInternet(getActivity())) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("sync_time", "2014-04-04");
            client.post("http://52.89.156.64/index.php/api/top_artist_new", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (progressbar.getVisibility() == View.VISIBLE)
                        progressbar.setVisibility(View.GONE);
                    if (CommunicationLayer.parseTopArtistListData(response) == 1) {
                        getDBData();
                        if (artistTop.size() > 0) {

                            MoArtist artist = new MoArtist();
                            artist.setId(0);
                            artistTop.add(artist);
                            layArtist.addView(prepareTextView("TOP ARTISTS"));
                            layArtist.addView(makeHorizentalGridArtists(artistTop));
                            layArtist.addView(prepareTextViewInside());
                            layArtist.addView(prepareMoreViewArtists());
                            layArtist.addView(prepareTextViewInside());

                        }
                    }
                }
            });

        }
        if (InternetConnectivity.isConnectedToInternet(getActivity())) {
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(Urls.BASE_URL + "index.php/api/top_album", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (CommunicationLayer.parseTopAlbumData(response) == 1) {
                        getDBData();
                        if (albumsTop.size() > 0) {

                            MoAlbum album = new MoAlbum();
                            album.setId(0);
                            albumsTop.add(album);
                            layTop.addView(prepareTextView("TOP RATED ALBUMS"));
                            layTop.addView(makeHorizentalGrid(albumsTop));
                            layTop.addView(prepareTextViewInside());
                            layTop.addView(prepareMoreViewAlbums());
                            layTop.addView(prepareTextViewInside());
                        }
                    }
                }
            });

        }

        if (InternetConnectivity.isConnectedToInternet(getActivity())) {
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(Urls.BASE_URL + "index.php/api/new_releas", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    //if (CommunicationLayer.parseNewReleaseDate(response) == 1) {
                        Globals.releaseDateLimit = CommunicationLayer.parseNewReleaseDate(response);
                        getDBData();
                        if (albumsUp.size() > 0) {

                            MoAlbum album = new MoAlbum();
                            album.setId(0);
                            albumsUp.add(album);
                            layNewRelease.addView(prepareTextView("NEW RELEASED ALBUMS"));
                            layNewRelease.addView(makeHorizentalGrid(albumsUp));
                            layNewRelease.addView(prepareTextViewInside());
                            layNewRelease.addView(prepareMoreViewNewRelease());
                            layNewRelease.addView(prepareTextViewInside());
                        }

                    //}
                }
            });

        }

        if (InternetConnectivity.isConnectedToInternet(getActivity())) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.add("sync_time", "2015-04-04");
            client.post("http://52.89.156.64/index.php/api/get_genre_list", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.e("GENRE", response.toString());

                    if (CommunicationLayer.parseGenreListData(response) == 1) {
                        getDBData();
                        if (albumsMoods.size() > 0) {
                            layMoods.addView(prepareTextView("MOODS & GENRES"));
                            int lenGenres = albumsMoods.size();
                                /*if(lenGenres>10){
                                    lenGenres=10;
                                }*/
                            for (int i = 0; i < lenGenres; i = i + 2) {
                                MoGenres moGenres1 = new MoGenres();
                                moGenres1.setId1(albumsMoods.get(i).getId());
                                moGenres1.setTitle1(albumsMoods.get(i).getTitle());
                                moGenres1.setImgUrl1(albumsMoods.get(i).getImgUrl());
                                if ((i + 1) < lenGenres) {
                                    moGenres1.setId2(albumsMoods.get(i + 1).getId());
                                    moGenres1.setTitle2(albumsMoods.get(i + 1).getTitle());
                                    moGenres1.setImgurl2(albumsMoods.get(i + 1).getImgUrl());
                                }

                                layMoods.addView(prepareViewMoodsGenres(moGenres1));
                            }

                        }
                    }
                }
            });

        } else {

        }



    }

    public void prepareDisplay() {

        getDBData();

        if (albumsTop.size() > 0) {

            MoAlbum album = new MoAlbum();
            album.setId(0);
            albumsTop.add(album);
            layTop.addView(prepareTextView("TOP RATED ALBUMS"));
            layTop.addView(makeHorizentalGrid(albumsTop));
            layTop.addView(prepareTextViewInside());
            layTop.addView(prepareMoreViewAlbums());
            layTop.addView(prepareTextViewInside());
        }

        if (albumsUp.size() > 0) {

            MoAlbum album = new MoAlbum();
            album.setId(0);
            albumsUp.add(album);
            layNewRelease.addView(prepareTextView("NEW RELEASED ALBUMS"));
            layNewRelease.addView(makeHorizentalGrid(albumsUp));
            layNewRelease.addView(prepareTextViewInside());
            layNewRelease.addView(prepareMoreViewNewRelease());
            layNewRelease.addView(prepareTextViewInside());
        }

        if (artistTop.size() > 0) {

            MoArtist artist = new MoArtist();
            artist.setId(0);
            artistTop.add(artist);
            layArtist.addView(prepareTextView("TOP ARTISTS"));
            layArtist.addView(makeHorizentalGridArtists(artistTop));
            layArtist.addView(prepareTextViewInside());
            layArtist.addView(prepareMoreViewArtists());
            layArtist.addView(prepareTextViewInside());

        }

        if (albumsMoods.size() > 0) {
            layMoods.addView(prepareTextView("MOODS & GENRES"));
            int lenGenres = albumsMoods.size();
                                /*if(lenGenres>10){
                                    lenGenres=10;
                                }*/
            for (int i = 0; i < lenGenres; i = i + 2) {
                MoGenres moGenres1 = new MoGenres();
                moGenres1.setId1(albumsMoods.get(i).getId());
                moGenres1.setTitle1(albumsMoods.get(i).getTitle());
                moGenres1.setImgUrl1(albumsMoods.get(i).getImgUrl());
                if ((i + 1) < lenGenres) {
                    moGenres1.setId2(albumsMoods.get(i + 1).getId());
                    moGenres1.setTitle2(albumsMoods.get(i + 1).getTitle());
                    moGenres1.setImgurl2(albumsMoods.get(i + 1).getImgUrl());
                }

                layMoods.addView(prepareViewMoodsGenres(moGenres1));
            }

        }
    }

    private TextView prepareTextView(String text) {
        TextView tv = new TextView(context);
        tv.setText(text);
        //tv.getFontFeatureSettings();
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(11);
        tv.setTextColor(Color.parseColor("#808080"));
        //tv.setTextColor(context.getResources().getColor(R.color.app_bar));
        //tv.setBackgroundColor(context.getResources().getColor(R.color.white));
        tv.setBackgroundColor(Color.parseColor("#E8E8E8"));

        //tv.setTypeface(Typeface.DEFAULT_BOLD);

        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setPadding(CommonFunc.getPixel(context, 10), CommonFunc.getPixel(context, 10), 0, CommonFunc.getPixel(context, 10));
        return tv;
    }

    private TextView prepareTextViewInside() {
        TextView tv = new TextView(context);
        //tv.setBackgroundColor(context.getResources().getColor(R.color.app_bar));
        tv.setBackgroundColor(Color.parseColor("#dfdfdf"));
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
        return tv;
    }


    private LinearLayout prepareMoreViewArtists() {
        LinearLayout laymore = (LinearLayout) inflater.inflate(R.layout.row_more, null);
        //laymore.setPadding(0, 10, 0, 10);
        TextView textViewMore = (TextView) laymore.findViewById(R.id.tv_more);
        ImageView imageViewMore = (ImageView) laymore.findViewById(R.id.img_more);

        textViewMore.setText("All Artists");
        textViewMore.setTextSize(13);
        textViewMore.setTypeface(Typeface.DEFAULT_BOLD);
        textViewMore.setTextColor(Color.parseColor("#303030"));
        imageViewMore.setImageResource(R.drawable.more_icon);
        laymore.setBackgroundResource(R.drawable.btn_press);
        laymore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).replaceFrag(FragArtistView.getInstance(), "All Artists");
                Log.d("Sajal", "2 clicked");
            }
        });
        return laymore;

    }

    private LinearLayout prepareMoreViewAlbums() {
        LinearLayout laymore = (LinearLayout) inflater.inflate(R.layout.row_more, null);
        //laymore.setPadding(0, 10, 0, 10);

        TextView textViewMore = (TextView) laymore.findViewById(R.id.tv_more);
        ImageView imageViewMore = (ImageView) laymore.findViewById(R.id.img_more);
        textViewMore.setTextSize(13);
        textViewMore.setTypeface(Typeface.DEFAULT_BOLD);
        textViewMore.setTextColor(Color.parseColor("#303030"));
        textViewMore.setText("See All");
        imageViewMore.setImageResource(R.drawable.more_icon);
        laymore.setBackgroundResource(R.drawable.btn_press);
        laymore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).replaceFrag(FragAlbum.getInstance(), "Albums");
                Log.d("Sajal", "2 clicked");
            }
        });
        return laymore;

    }


    private LinearLayout prepareMoreViewNewRelease() {
        LinearLayout laymore = (LinearLayout) inflater.inflate(R.layout.row_more, null);
        //laymore.setPadding(0, 10, 0, 10);
        TextView textViewMore = (TextView) laymore.findViewById(R.id.tv_more);
        ImageView imageViewMore = (ImageView) laymore.findViewById(R.id.img_more);
        textViewMore.setTextSize(13);
        textViewMore.setTypeface(Typeface.DEFAULT_BOLD);
        textViewMore.setTextColor(Color.parseColor("#303030"));
        textViewMore.setText("See All");
        imageViewMore.setImageResource(R.drawable.more_icon);
        laymore.setBackgroundResource(R.drawable.btn_press);
        laymore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).replaceFrag(FragNewRelease.getInstance(), "New Releases");
                Log.d("Sajal", "2 clicked");
            }
        });
        return laymore;

    }

    private LinearLayout prepareViewMoodsGenres(final MoGenres moGenres) {

        LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.row_home_moods_genres, null);
        lay.setPadding(0, 12, 0, 12);
        lay.setBackgroundColor(Color.parseColor("#fafafa"));
        FrameLayout frameLayout1 = (FrameLayout) lay.findViewById(R.id.row_albumhome1);
        FrameLayout frameLayout2 = (FrameLayout) lay.findViewById(R.id.row_albumhome2);

        TextView textView1 = (TextView) lay.findViewById(R.id.tv_new_release_title1);
        TextView textView2 = (TextView) lay.findViewById(R.id.tv_new_release_title2);
        textView1.setText(moGenres.getTitle1());
        textView2.setText(moGenres.getTitle2());
        frameLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).replaceFrag(FragExpandableListGenres.getInstance(moGenres.getId1(), 1), moGenres.getTitle1());
                Log.d("Sajal", "1 clicked" + moGenres.getTitle1());

            }
        });
        frameLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).replaceFrag(FragExpandableListGenres.getInstance(moGenres.getId2(), 1), moGenres.getTitle2());
                Log.d("Sajal", "2 clicked" + moGenres.getTitle2());
            }
        });
        ImageView imageView1 = (ImageView) lay.findViewById(R.id.img_new_albums1);
        ImageView imageView2 = (ImageView) lay.findViewById(R.id.img_new_albums2);

        String imgUrl1 = TextUtils.isEmpty(moGenres.getImgUrl1()) ? Urls.BASE_URL + Urls.IMG_GENRE + "6e83e5d5fee89ad93c147322a1314076.jpg" : Urls.BASE_URL + Urls.IMG_GENRE + moGenres.getImgUrl1();

        Picasso.with(context)
                .load(imgUrl1)
                .placeholder(R.drawable.main_myplaylist)
                .into(imageView1);

        String imgUrl2 = TextUtils.isEmpty(moGenres.getImgurl2()) ? Urls.BASE_URL + Urls.IMG_GENRE + "6e83e5d5fee89ad93c147322a1314076.jpg" : Urls.BASE_URL + Urls.IMG_GENRE + moGenres.getImgurl2();
        Picasso.with(context)
                .load(imgUrl2)
                .placeholder(R.drawable.main_myplaylist)
                .into(imageView2);


        return lay;


    }



    private View makeHorizentalGridArtists(ArrayList<MoArtist> artists) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        HorizontalScrollView hScroll = new HorizontalScrollView(context);

        LinearLayout lay = new LinearLayout(context);
        lay.setLayoutParams(params);
        lay.setPadding(20, 20, 20, 20);
        lay.setBackgroundColor(Color.parseColor("#f6f6f6"));
        GridView grid = new GridView(context);
        int width = CommonFunc.getPixel(context, 150) * artists.size();

        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        grid.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        grid.setNumColumns(artists.size());
        grid.setBackgroundColor(Color.parseColor("#f6f6f6"));
        grid.setHorizontalSpacing(CommonFunc.getPixel(context, 5));

        grid.setHorizontalFadingEdgeEnabled(true);
        adArtist = new HAdArtist(context, R.layout.row_artist, artists);
        grid.setAdapter(adArtist);

        lay.addView(grid);
        hScroll.addView(lay);

        Log.d("Sajal", artists.size() + " size on make");

        return hScroll;


    }


    private View makeHorizentalGrid(ArrayList<MoAlbum> albums) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        HorizontalScrollView hScroll = new HorizontalScrollView(context);

        LinearLayout lay = new LinearLayout(context);
        lay.setLayoutParams(params);
        lay.setPadding(20, 20, 20, 20);
        lay.setBackgroundColor(Color.parseColor("#f6f6f6"));
        GridView grid = new GridView(context);
        int width = CommonFunc.getPixel(context, 150) * albums.size();
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        grid.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        grid.setNumColumns(albums.size());
        grid.setBackgroundColor(Color.parseColor("#f6f6f6"));
        grid.setHorizontalSpacing(CommonFunc.getPixel(context, 5));


        adapter = new HAdAlbum(context, R.layout.row_home, albums);
        grid.setAdapter(adapter);

        lay.addView(grid);
        hScroll.addView(lay);

        Log.d("Sajal", albums.size() + " size on make");

        return hScroll;


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