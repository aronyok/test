package baajna.scroll.owner.mobioapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.connection.InternetConnectivity;
import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;
import baajna.scroll.owner.mobioapp.adapter.AdExpandableList;
import baajna.scroll.owner.mobioapp.datamodel.MoGenres;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.utils.Globals;
import baajna.scroll.owner.mobioapp.utils.SparkleApp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jewel on 1/24/2016.
 */
public class FragExpandableListGenre extends Fragment {
    private static final String TYPE = "type", GENRE = "genre_id";
    MoGenres genre;

    AdExpandableList listAdapter;
    ExpandableListView expandableListView;
    List<MoSong> listDataHeader;
    ImageView imgSongDetails;
    ImageView img_Expandable;
    LinearLayout lay_song_download, lay_song_details;
    private int lastExpandedPosition = -1, genreId, type;
    private HashMap<String, List<String>> listDataChild;
    private View view;
    private Context context;

    public static FragExpandableListGenre getInstance(int genreId, int type) {
        FragExpandableListGenre fragExpandable = new FragExpandableListGenre();
        Bundle bundle = new Bundle();
        bundle.putInt(GENRE, genreId);
        bundle.putInt(TYPE, type);
        fragExpandable.setArguments(bundle);
        return fragExpandable;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lay_expandable_song_list, container, false);
        context = getActivity();
        init();

        return view;
    }


    private void init() {
        // get the listview
        expandableListView = (ExpandableListView) view.findViewById(R.id.exp_list_view);
        img_Expandable = (ImageView) view.findViewById(R.id.img_expandable_album);
        imgSongDetails = (ImageView) view.findViewById(R.id.img_song_details);
        lay_song_download = (LinearLayout) view.findViewById(R.id.lay_song_download);
        lay_song_details = (LinearLayout) view.findViewById(R.id.lay_song_details);
        listDataHeader = new ArrayList<MoSong>();
        listDataChild = new HashMap<String, List<String>>();

        genreId = getArguments().getInt(GENRE);
        type = getArguments().getInt(TYPE);

        if (InternetConnectivity.isConnectedToInternet(getContext())) {
            loadData();
        } else {
            prepareListData();
        }


    }


    private void loadData() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("genre_id", genreId);
        params.put("start", "0");
        params.put("sync_time", "2015-04-04");
        client.post("http://52.89.156.64/index.php/api/genre_wise_songs_list", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("SONG", response.toString());
                Log.e("SONG", "id " + genreId);

                if (CommunicationLayer.parseGenreWiseSongPost(response) == 1) {
                    if (SparkleApp.getInstance().getSongList() != null) {

                        Globals.playlist = SparkleApp.getInstance().getSongList();
                           /* DbManager mydb = new DbManager(getActivity());
                            mydb.addSongs(SparkleApp.getInstance().getSongList());
                            mydb.close();*/

                    } else {

                    }
                } else {

                }

                prepareListData();
            }
        });

    }

    private void getDBData() {
        DbManager db = new DbManager(getContext());
        genre = new MoGenres();
        genre = db.getGenre(genreId);
        if (type == Globals.TYPE_EXP_PLAYLIST) {
            listDataHeader = db.getSongs(genreId);
        } else {
            listDataHeader = db.getSongsByGenres(genreId);
        }
        Log.e("GENRE"," "+genreId);
        Log.e("GENRE"," "+listDataHeader);
        db.close();

    }

    private void prepareListData() {


        //listDataHeader = SparkleApp.getInstance().getSongList();
        getDBData();
        listAdapter = new AdExpandableList(context, listDataHeader, listDataChild);

        // setting list adapter
        expandableListView.setAdapter(listAdapter);


        // Listview Group click listener
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        // Listview Group collasped listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });


        // Listview on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub


                return true;
            }
        });


    }
}
