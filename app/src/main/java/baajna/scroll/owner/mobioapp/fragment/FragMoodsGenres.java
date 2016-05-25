package baajna.scroll.owner.mobioapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.adapter.AdMoodsGenres;
import baajna.scroll.owner.mobioapp.connection.InternetConnectivity;
import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;
import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;
import baajna.scroll.owner.mobioapp.utils.SpacesItemDecoration;
import baajna.scroll.owner.mobioapp.utils.Urls;
import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jewel on 1/24/2016.
 */
public class FragMoodsGenres extends Fragment {
    private RecyclerView recyclerView;
    private AdMoodsGenres adapter;
    private View view;
    private ArrayList<MoAlbum> albums;


    public static FragMoodsGenres getInstance() {
        FragMoodsGenres fragAlbum = new FragMoodsGenres();
        return fragAlbum;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_recycler, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        setListener();
        loadData();
    }


    private void initViews() {
        albums = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new AdMoodsGenres(getActivity()) {
            @Override
            public void onAfterClick(View view, int position) {

            }
        };
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));
        recyclerView.setAdapter(adapter);


    }

    private void setListener() {


    }

    private void loadData() {
        if (InternetConnectivity.isConnectedToInternet(getActivity())) {
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(Urls.BASE_URL + "index.php/api/album_list", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (CommunicationLayer.parseAlbumListPost(response) == 1) {
                        prepareDis();

                    }

                }
            });


        } else {
            prepareDis();
        }
    }

    private void prepareDis(){
        DbManager myDb = new DbManager(getActivity());
        albums = myDb.getAlbums(DbManager.SQL_ALBUMS_TOP);
        myDb.close();
        adapter.setData(albums);
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