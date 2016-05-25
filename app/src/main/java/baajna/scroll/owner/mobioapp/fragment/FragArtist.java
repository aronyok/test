package baajna.scroll.owner.mobioapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.utils.MyApp;
import baajna.scroll.owner.mobioapp.utils.Urls;
import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.adapter.AdArtist;
import baajna.scroll.owner.mobioapp.connection.InternetConnectivity;
import baajna.scroll.owner.mobioapp.datamodel.MoArtist;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;
import baajna.scroll.owner.mobioapp.utils.SpacesItemDecoration;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jewel on 1/24/2016.
 */
public class FragArtist extends Fragment implements SearchView.OnQueryTextListener{
    private RecyclerView recyclerView;
    private AdArtist adapter;
    private View view;
    private ArrayList<MoArtist> artists;


    public static FragArtist getInstance() {

        FragArtist fragArtist = new FragArtist();
        return fragArtist;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_recycler, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        initViews();
        loadData();
    }


    private void initViews() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        artists = new ArrayList<>();
        adapter = new AdArtist(getActivity());
        recyclerView.addItemDecoration(new SpacesItemDecoration(16));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(adapter);



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
                        adapter.setFilter(artists);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<MoArtist> filteredModelList = filter(artists, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<MoArtist> filter(ArrayList<MoArtist> artists, String query) {
        query = query.toLowerCase();

        final ArrayList<MoArtist> filteredModelList = new ArrayList<>();
        for (MoArtist artist : artists) {
            final String text = artist.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(artist);
            }
        }
        return filteredModelList;
    }




    private void loadData() {
        if (InternetConnectivity.isConnectedToInternet(getActivity())) {
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(Urls.BASE_URL + "index.php/api/artist_list", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.e("ARTIST_LIST",response.toString() );
                    if (CommunicationLayer.parseArtistListData(response) == 1) {
                        prepareDis();
                    }


                }
            });


        } else {
            prepareDis();
        }
    }

    private void prepareDis(){
        DbManager db=new DbManager(MyApp.getAppContext());
        artists = db.getArtsts(DbManager.SQL_ARTISTS);
        Log.d("Jewel",artists.size()+" artist");
        if (artists.size() > 0)
            view.findViewById(R.id.tv_empty).setVisibility(View.GONE);
        else
            view.findViewById(R.id.tv_empty).setVisibility(View.VISIBLE);
        db.close();

        adapter.setData(artists);
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