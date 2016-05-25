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
import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;
import baajna.scroll.owner.mobioapp.adapter.AdAlbumSearch;

import java.util.ArrayList;

/**
 * Created by ARONYOK on 2/16/2016.
 */
public class FragSearchAlbum extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerview;
    private ArrayList<MoAlbum> albums;
    private AdAlbumSearch adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.lay_search, container, false);

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview_search);
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        //recyclerview.setLayoutManager(layoutManager);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        //String[] locales = Locale.getISOCountries();
        albums = new ArrayList<>();

        /*for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            albums.add(new MoAlbum(obj.getDisplayCountry(), obj.getISO3Country()));
        }*/
        for(int i=0;i<50;i++){
            MoAlbum moAlbum=new MoAlbum();
            moAlbum.setId(i);
            moAlbum.setTitle("Album name " + i);
            albums.add(moAlbum);

        }

        adapter = new AdAlbumSearch(getContext()) {
            @Override
            public void onAfterClick(View view, int position) {

            }
        };
        adapter.setData(albums);
        recyclerview.setAdapter(adapter);
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

    private ArrayList<MoAlbum> filter(ArrayList<MoAlbum> models, String query) {
        query = query.toLowerCase();

        final ArrayList<MoAlbum> filteredModelList = new ArrayList<>();
        for (MoAlbum model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}