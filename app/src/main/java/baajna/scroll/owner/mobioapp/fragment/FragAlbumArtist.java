package baajna.scroll.owner.mobioapp.fragment;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.adapter.AdAlbum;
import baajna.scroll.owner.mobioapp.adapter.AdPopupList;
import baajna.scroll.owner.mobioapp.connection.InternetConnectivity;
import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;
import baajna.scroll.owner.mobioapp.datamodel.MoPlayList;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;
import baajna.scroll.owner.mobioapp.services.MusicService;
import baajna.scroll.owner.mobioapp.utils.MyApp;
import baajna.scroll.owner.mobioapp.utils.SpacesItemDecoration;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jewel on 1/24/2016.
 */
public class FragAlbumArtist extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private AdAlbum adapter;
    private View view;
    private ArrayList<MoAlbum> albums;
    private MusicService musicService;
    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService=((MusicService.MyBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService=null;
        }
    };
    public static FragAlbumArtist getInstance()
    {
        FragAlbumArtist fragAlbum = new FragAlbumArtist();
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
        setHasOptionsMenu(true);
        initViews();
        setListener();
        loadData();
    }
    @Override
    public void onResume() {
        super.onResume();
        Intent intent=new Intent(getContext(),MusicService.class);
        intent.setAction(MusicService.NORMAL_ACTION);
        getContext().startService(intent);
        getContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unbindService(serviceConnection);
    }

    private void initViews() {
        albums = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new AdAlbum(getActivity()) {
            @Override
            public void onAfterClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.lay_new_release_options:
                        showPopup(view, position);
                        break;
                }
            }
        };
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
            RequestParams params=new RequestParams();
            params.add("artist_id","23");
            params.add("start","0");
            params.add("sync_time","2016-04-04");
            client.post("http://52.89.156.64/index.php/api/get_artist_wise_album",params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.e("ALBUM", response.toString());
                    if (CommunicationLayer.parseArtistAlbumPost(response) == 1) {
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

    private void showPopup(View anchorView, final int position) {

        final int viewPos = position;
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_layout, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                location[0], location[1] + anchorView.getHeight());

        TextView tvPlayAll = (TextView) popupView.findViewById(R.id.tv_play_all);
        TextView tvAddQueue = (TextView) popupView.findViewById(R.id.tv_add_to_queue);
        TextView tvPlayList = (TextView) popupView.findViewById(R.id.tv_add_to_playlist);
        TextView tvDownload = (TextView) popupView.findViewById(R.id.tv_download);

        tvPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int albumId = albums.get(viewPos).getId();

                DbManager db = new DbManager(getContext());
                ArrayList<MoSong> songs = db.getSongsByAlbum(albumId);
                ArrayList<MoPlayList> playLists = new ArrayList<MoPlayList>();
                MoPlayList playList;
                for (int i = 0; i < songs.size(); i++) {
                    playList = new MoPlayList();
                    playList.setId(0);
                    playList.setSongId(songs.get(i).getId());

                    playLists.add(playList);
                }
                db.addPlayLists(playLists);
                MusicService.songs = db.getSongs(DbManager.SQL_SONGS_PLAYLIST_RUNNING);
                db.close();

                for (int i = 0; i < MusicService.songs.size(); i++) {
                    if (songs.get(0).getId() == MusicService.songs.get(i).getId()) {
                        MusicService.songPosn = i;
                        break;
                    }
                }
                if (MusicService.isRunning) {
                    musicService.playSong(MusicService.songPosn);
                } else {

                    Intent startIntent = new Intent(getContext(), MusicService.class);
                    startIntent.setAction(MusicService.MAIN_ACTION);
                    getContext().startService(startIntent);
                }
                Toast.makeText(MyApp.getAppContext(), "Added to music player", Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
            }
        });
        tvAddQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int albumId = albums.get(viewPos).getId();

                DbManager db = new DbManager(getContext());
                ArrayList<MoSong> songs = db.getSongsByAlbum(albumId);
                ArrayList<MoPlayList> playLists = new ArrayList<MoPlayList>();
                MoPlayList playList;
                for (int i = 0; i < songs.size(); i++) {
                    playList = new MoPlayList();
                    playList.setId(0);
                    playList.setSongId(songs.get(i).getId());

                    playLists.add(playList);
                }
                db.addPlayLists(playLists);
                db.close();
                Toast.makeText(MyApp.getAppContext(), "Added player queue", Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
            }
        });

        tvPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.popup_playlist);
                dialog.setTitle("Add to playlist");

                DbManager db = new DbManager(getContext());
                final ArrayList<MoPlayList> playList1 = db.getPlayListName();
                db.close();


                RecyclerView listView = (RecyclerView) dialog.findViewById(R.id.recycler_view);
                listView.setLayoutManager(new LinearLayoutManager(getContext()));
                AdPopupList adapter = new AdPopupList(getContext()) {
                    @Override
                    public void onAfterClick(int position) {
                        int playlistId = playList1.get(position).getId();
                        int albumId = albums.get(viewPos).getId();
                        DbManager db = new DbManager(getContext());
                        ArrayList<MoSong> songs = db.getSongsByAlbum(albumId);

                        ArrayList<MoPlayList> playLists = new ArrayList<MoPlayList>();
                        MoPlayList playList;
                        for (int i = 0; i < songs.size(); i++) {
                            playList = new MoPlayList();
                            playList.setId(playlistId);
                            playList.setSongId(songs.get(i).getId());

                            playLists.add(playList);
                        }
                        db.addPlayLists(playLists);
                        db.close();
                        Log.d("Jewel", "a " + playLists.size() + ": " + albumId);
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
                        final Dialog mDialog = new Dialog(getContext());
                        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        mDialog.setContentView(R.layout.popup_create_playlist);
                        Button btnSave = (Button) mDialog.findViewById(R.id.btn_save);

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
                                } else {
                                    Toast.makeText(getContext(), "Empty name !!", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                        mDialog.show();
                    }
                });
                dialog.show();
            }
        });

    }

}