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

import baajna.scroll.owner.mobioapp.adapter.AdAlbumSearch;
import baajna.scroll.owner.mobioapp.connection.InternetConnectivity;
import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;
import baajna.scroll.owner.mobioapp.utils.SpacesItemDecoration;
import baajna.scroll.owner.mobioapp.utils.Urls;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jewel on 1/24/2016.
 */
public class FragAlbumSearch extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private AdAlbumSearch adapter;
    private View view;
    private ArrayList<MoAlbum> albums;


    public static FragAlbumSearch getInstance() {
        FragAlbumSearch fragAlbum = new FragAlbumSearch();
        return fragAlbum;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lay_search, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_search);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        initViews();

        //loadData();
    }


    private void initViews() {
        albums = new ArrayList<>();
        loadData();
        adapter = new AdAlbumSearch(getActivity()) {
            @Override
            public void onAfterClick(View view, int position) {
                /*switch (view.getId()) {
                    case R.id.img_new_release_options:
                        showPopup(view, position);
                        break;
                }*/
            }
        };

        recyclerView.addItemDecoration(new SpacesItemDecoration(20));
        recyclerView.setAdapter(adapter);


    }

    private void setListener() {


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

    private void prepareDis() {
        DbManager myDb = new DbManager(getActivity());
        albums = myDb.getAlbums(DbManager.SQL_ALBUMS_TOP);
        myDb.close();
        adapter.setData(albums);
    }


}