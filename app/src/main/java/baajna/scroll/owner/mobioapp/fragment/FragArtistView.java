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
import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.connection.InternetConnectivity;
import baajna.scroll.owner.mobioapp.datamodel.MoArtist;
import baajna.scroll.owner.mobioapp.datamodel.MoGenres;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;
import baajna.scroll.owner.mobioapp.utils.CommonFunc;
import baajna.scroll.owner.mobioapp.utils.Urls;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FragArtistView extends Fragment {
    private static FragArtistView fragArtistView;
    //private static FragHomePage fragHomePage;
    private LinearLayout mainLay, layTop, layTrend, layMoods, layMidle;
    private RecyclerView recyclerView;
    //private HAdArtist adapter;
    private Context context;
    private View view;
    //private ArrayList<MoAlbum> albumsUp, albumsTop, albumsMoods;
    private ArrayList<MoArtist> artistTop, artistTrend,artists ;
    private ArrayList<MoGenres> genres;
    private LayoutInflater inflater;
    private AdView mAdView;

    public static FragArtistView getInstance() {

        fragArtistView = new FragArtistView();
        return fragArtistView;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lay_home, container, false);
        initViews();
        if (InternetConnectivity.isConnectedToInternet(getContext())) {
            loadData();
        } else {
            prepareDisplay();
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void initViews() {
        context = getActivity();
        inflater = LayoutInflater.from(context);

        artistTop = new ArrayList<>();
        artistTrend = new ArrayList<>();
        //albumsMoods = new ArrayList<>();
        artists = new ArrayList<>();
        mainLay = (LinearLayout) view.findViewById(R.id.layMain);
        layTop = new LinearLayout(getContext());
        layTop.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layTop.setOrientation(LinearLayout.VERTICAL);

        layTrend = new LinearLayout(getContext());
        layTrend.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layTrend.setOrientation(LinearLayout.VERTICAL);

        mainLay.addView(layTop);

        mainLay.addView(layTrend);

        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);


    }

    private void getDBData() {

        DbManager db = new DbManager(getContext());
        artists = db.getArtsts(DbManager.SQL_ARTISTS);
        artistTop = db.getTopArtists(DbManager.SQL_TOP_ARTISTS);
        artistTrend = db.getTrendArtists(DbManager.SQL_TREND_ARTISTS);
        db.close();

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

                    if (CommunicationLayer.parseTopArtistListData(response) == 1) {

                        //prepareDisplay();
                        getDBData();
                        if (artistTop.size() > 0) {
                            layTop.addView(prepareMoreViewArtists());
                            layTop.addView(prepareTextView("TOP ARTISTS"));
                            //layTop.addView(prepareViewArtist(artistTop));
                            int lenArtist = artistTop.size();
                            if (lenArtist > 10) {
                                lenArtist = 10;
                            }
                            for (int i = 0; i < lenArtist; i = i + 2) {
                                MoArtist moArtist1 = new MoArtist();
                                moArtist1.setId1(artistTop.get(i).getId());
                                moArtist1.setArtistName1(artistTop.get(i).getName());
                                moArtist1.setCoverImgUrl1(artistTop.get(i).getImge());

                                if ((i + 1) < lenArtist) {
                                    moArtist1.setId2(artistTop.get(i + 1).getId());
                                    moArtist1.setArtistName2(artistTop.get(i + 1).getName());
                                    moArtist1.setCoverImgUrl2(artistTop.get(i + 1).getImge());
                                }


                                layTop.addView(prepareViewArtist(moArtist1));
                            }
                            //layTop.addView(prepareViewArtist(moArtist2));
                            //layTop.addView(prepareViewArtist(moArtist3));

                        }
                    }
                }
            });

        }


        if (InternetConnectivity.isConnectedToInternet(getActivity())) {
            AsyncHttpClient client = new AsyncHttpClient();
            client.post("http://52.89.156.64/index.php/api/trending_artist", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (CommunicationLayer.parseTrendingArtistListData(response) == 1) {
                        //prepareDisplay();
                        getDBData();
                        if (artistTrend.size() > 0) {

                            layTrend.addView(prepareTextView("TRENDING ARTISTS"));
                            //layTrend.addView(prepareViewArtist(artistTrend));
                            Log.d("Sajal", artistTrend.size() + "artistTrend");
                            int lenArtist = artistTrend.size();
                            if (lenArtist > 10) {
                                lenArtist = 10;
                            }
                            for (int i = 0; i < lenArtist; i = i + 2) {
                                MoArtist moArtist1 = new MoArtist();
                                moArtist1.setId1(artistTrend.get(i).getId());
                                moArtist1.setArtistName1(artistTrend.get(i).getName());
                                moArtist1.setCoverImgUrl1(artistTrend.get(i).getImge());
                                if ((i + 1) < lenArtist) {
                                    moArtist1.setId2(artistTrend.get(i + 1).getId());
                                    moArtist1.setArtistName2(artistTrend.get(i + 1).getName());
                                    moArtist1.setCoverImgUrl2(artistTrend.get(i + 1).getImge());
                                }
                                layTrend.addView(prepareViewArtist(moArtist1));
                            }
                            //layTrend.addView(prepareViewArtist(moArtist2));
                            //layTrend.addView(prepareViewArtist(moArtist3));
                            //layTrend.addView(prepareMoreViewArtists());

                        }
                    }
                }
            });

        } else {

        }
        //prepareDisplay();
    }

    public void prepareDisplay() {
        getDBData();

        if (artistTop.size() > 0) {
            layTop.addView(prepareMoreViewArtists());
            layTop.addView(prepareTextView("TOP ARTISTS"));
            //layTop.addView(prepareViewArtist(artistTop));
            int lenArtist = artistTop.size();
            if (lenArtist > 10) {
                lenArtist = 10;
            }
            for (int i = 0; i < lenArtist; i = i + 2) {
                MoArtist moArtist1 = new MoArtist();
                moArtist1.setId1(artistTop.get(i).getId());
                moArtist1.setArtistName1(artistTop.get(i).getName());
                moArtist1.setCoverImgUrl1(artistTop.get(i).getImge());

                if ((i + 1) < lenArtist) {
                    moArtist1.setId2(artistTop.get(i + 1).getId());
                    moArtist1.setArtistName2(artistTop.get(i + 1).getName());
                    moArtist1.setCoverImgUrl2(artistTop.get(i + 1).getImge());
                }


                layTop.addView(prepareViewArtist(moArtist1));
            }
            //layTop.addView(prepareViewArtist(moArtist2));
            //layTop.addView(prepareViewArtist(moArtist3));

        }

        if (artistTrend.size() > 0) {

            layTrend.addView(prepareTextView("TRENDING ARTISTS"));
            //layTrend.addView(prepareViewArtist(artistTrend));
            Log.d("Sajal", artistTrend.size() + "artistTrend");
            int lenArtist = artistTrend.size();
            if (lenArtist > 10) {
                lenArtist = 10;
            }
            for (int i = 0; i < lenArtist; i = i + 2) {
                MoArtist moArtist1 = new MoArtist();
                moArtist1.setId1(artistTrend.get(i).getId());
                moArtist1.setArtistName1(artistTrend.get(i).getName());
                moArtist1.setCoverImgUrl1(artistTrend.get(i).getImge());
                if ((i + 1) < lenArtist) {
                    moArtist1.setId2(artistTrend.get(i + 1).getId());
                    moArtist1.setArtistName2(artistTrend.get(i + 1).getName());
                    moArtist1.setCoverImgUrl2(artistTrend.get(i + 1).getImge());
                }
                layTrend.addView(prepareViewArtist(moArtist1));
            }
            //layTrend.addView(prepareViewArtist(moArtist2));
            //layTrend.addView(prepareViewArtist(moArtist3));
            //layTrend.addView(prepareMoreViewArtists());

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

    private TextView prepareTextViewInside(String text) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextSize(2);
        tv.setTextColor(context.getResources().getColor(R.color.white));
        tv.setBackgroundColor(context.getResources().getColor(R.color.app_bar));
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return tv;
    }

    private LinearLayout prepareMoreViewArtists() {
        LinearLayout laymore = (LinearLayout) inflater.inflate(R.layout.row_more, null);
        //laymore.setPadding(0, 10, 0, 10);
        TextView textViewMore = (TextView) laymore.findViewById(R.id.tv_more);
        ImageView imageViewMore = (ImageView) laymore.findViewById(R.id.img_more);

        textViewMore.setText("See All Artists");
        textViewMore.setTextSize(13);
        textViewMore.setTypeface(Typeface.DEFAULT_BOLD);
        textViewMore.setTextColor(Color.parseColor("#303030"));
        imageViewMore.setImageResource(R.drawable.more_icon);
        laymore.setBackgroundResource(R.drawable.btn_press);
        laymore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).replaceFrag(FragArtist.getInstance(), "Artists");
                Log.d("Sajal", "2 clicked");
            }
        });
        return laymore;

    }

    private LinearLayout prepareViewArtist(final MoArtist moArtist) {

        LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.row_artist_top_trend, null);
        lay.setPadding(0, 10, 0, 10);
        FrameLayout frameLayout1 = (FrameLayout) lay.findViewById(R.id.row_albumhome1);
        FrameLayout frameLayout2 = (FrameLayout) lay.findViewById(R.id.row_albumhome2);

        TextView textView1 = (TextView) lay.findViewById(R.id.tv_new_release_title1);
        TextView textView2 = (TextView) lay.findViewById(R.id.tv_new_release_title2);
        textView1.setText(moArtist.getArtistName1());
        textView2.setText(moArtist.getArtistName2());
        frameLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MoGenres album=albumList.get(getAdapterPosition());

                ((MainActivity) context).replaceFrag(FragArtistSingleView.getInstance(moArtist.getId1(), 1), moArtist.getArtistName1());
                Log.d("Sajal", "1 clicked");
            }
        });
        frameLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).replaceFrag(FragArtistSingleView.getInstance(moArtist.getId2(), 1), moArtist.getArtistName2());
                Log.d("Sajal", "2 clicked");
            }
        });
        ImageView imageView1 = (ImageView) lay.findViewById(R.id.img_new_albums1);
        ImageView imageView2 = (ImageView) lay.findViewById(R.id.img_new_albums2);

        String imgUrl1 = TextUtils.isEmpty(moArtist.getCoverImgUrl1()) ? Urls.BASE_URL + Urls.IMG_ARTIST + "6e83e5d5fee89ad93c147322a1314076.jpg" : Urls.BASE_URL + Urls.IMG_ARTIST + moArtist.getCoverImgUrl1();
        //String imgUrl= moArtist.getImge().isEmpty()?Urls.BASE_URL+Urls.IMG_ALBUM +"6e83e5d5fee89ad93c147322a1314076.jpg":/*Urls.BASE_URL+Urls.IMG_ALBUM +*/artist.getImge();
        Picasso.with(context)
                .load(imgUrl1)
                .placeholder(R.drawable.main_myplaylist)
                .into(imageView1);

        String imgUrl2 = TextUtils.isEmpty(moArtist.getCoverImgUrl2()) ? Urls.BASE_URL + Urls.IMG_ARTIST + "6e83e5d5fee89ad93c147322a1314076.jpg" : Urls.BASE_URL + Urls.IMG_ARTIST + moArtist.getCoverImgUrl2();
        Picasso.with(context)
                .load(imgUrl2)
                .placeholder(R.drawable.main_myplaylist)
                .into(imageView2);

        return lay;


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