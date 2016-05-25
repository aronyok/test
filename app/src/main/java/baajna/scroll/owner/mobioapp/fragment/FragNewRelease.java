package baajna.scroll.owner.mobioapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;
import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;
import baajna.scroll.owner.mobioapp.utils.SpacesItemDecoration;
import baajna.scroll.owner.mobioapp.utils.Urls;
import baajna.scroll.owner.mobioapp.adapter.AdNewRelease;
import baajna.scroll.owner.mobioapp.connection.InternetConnectivity;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.utils.Globals;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jewel on 1/24/2016.
 */
public class FragNewRelease extends Fragment implements SearchView.OnQueryTextListener{
    private RecyclerView recyclerView;
    private AdNewRelease adapter;
    private View view;
    private ArrayList<MoAlbum> albums;

    public static FragNewRelease getInstance() {
        FragNewRelease fragNewRelease = new FragNewRelease();
        return fragNewRelease;

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
        getOnlineData();

    }


    private void initViews() {
        albums = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        adapter = new AdNewRelease(getActivity()) {
            @Override
            public void onAfterClick(View view, int position) {

            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));
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
                        adapter.setFilter(albums);
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
        final ArrayList<MoAlbum> filteredModelList = filter(albums, newText);

        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<MoAlbum> filter(ArrayList<MoAlbum> albums, String query) {
        query = query.toLowerCase();

        final ArrayList<MoAlbum> filteredModelList = new ArrayList<>();
        for (MoAlbum album : albums) {
            final String text = album.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(album);
            }
        }
        return filteredModelList;
    }


    private void getOnlineData() {
        if (InternetConnectivity.isConnectedToInternet(getActivity())) {
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(Urls.BASE_URL + "index.php/api/new_releas", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Globals.releaseDateLimit = CommunicationLayer.parseNewReleaseDate(response);
                    prepareDis();
                }
            });
        } else {
            prepareDis();
        }

    }

    private void prepareDis() {
        DbManager db = new DbManager(getActivity());
        albums=db.getNewAlbums(Globals.releaseDateLimit);
        adapter.setData(albums);
        db.close();
    }

}