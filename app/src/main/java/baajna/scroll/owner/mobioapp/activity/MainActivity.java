package baajna.scroll.owner.mobioapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mobioapp.baajna.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import baajna.scroll.owner.mobioapp.connection.InternetConnectivity;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.datamodel.MoSongCount;
import baajna.scroll.owner.mobioapp.fragment.FragAlbum;
import baajna.scroll.owner.mobioapp.fragment.FragArtistView;
import baajna.scroll.owner.mobioapp.fragment.FragDownload;
import baajna.scroll.owner.mobioapp.fragment.FragHomePage;
import baajna.scroll.owner.mobioapp.fragment.FragMyPlaylist;
import baajna.scroll.owner.mobioapp.fragment.FragNewRelease;
import baajna.scroll.owner.mobioapp.interfaces.OnUpdateUI;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.services.MusicService;
import baajna.scroll.owner.mobioapp.utils.CommonFunc;
import baajna.scroll.owner.mobioapp.utils.Globals;
import baajna.scroll.owner.mobioapp.utils.Urls;
import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {


    public ActionBar actionBar;
    public SlidingUpPanelLayout mLayout;
    private AlertDialog alertDialog;
    private ImageView imageView_SongBar, imgEdit, btnCancel, btnSave;
    private TextView tvSongTitle, tvSongAlbum;
    private Button songbarPlayButton;
    private Activity activity;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FragmentManager fm;
    private ArrayList<String> titles;
    private NavigationView navigationView;
    private String title, album, imageSong;
    private OnUpdateUI onUpdateUI;

    private MoSong runningSong;
    private int runningSongID;

    private Context context;

    public  MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        isStoragePermissionGranted();

        //callOnline();
    }

    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService=((MusicService.MyBinder)service).getService();
            DbManager mydb = new DbManager(MainActivity.this);
            musicService.setSongList(mydb.getSongs(DbManager.SQL_SONGS_PLAYLIST_RUNNING));
            mydb.close();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService=null;
            Log.e("SERVICE","discon");
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        startMusicService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(serviceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent=new Intent(context,MusicService.class);
        intent.setAction(MusicService.STARTFOREGROUND_ACTION);
        startService(intent);
    }

    private void init() {

        context = this;
        activity = this;
        syncSongInfo();
        fm = getSupportFragmentManager();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar = getSupportActionBar();


        alertDialog = new AlertDialog.Builder(activity).create();


        imageView_SongBar = (ImageView) findViewById(R.id.img_songbar_albumArt);
        tvSongTitle = (TextView) findViewById(R.id.tv_songbar_title);
        tvSongAlbum = (TextView) findViewById(R.id.tv_songbar_album);
        imgEdit = (ImageView) findViewById(R.id.img_edit);

        //imageView_SongBar.setImageDrawable(getResources().getDrawable(R.drawable.sync_icon));
        songbarPlayButton = (Button) findViewById(R.id.button_playpause);
        btnSave = (ImageView) findViewById(R.id.btn_save);
        btnCancel = (ImageView) findViewById(R.id.btn_cancel);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        if (titles == null)
            titles = new ArrayList<>();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {

                    case R.id.d_menu_home:
                        replaceFrag(FragHomePage.getInstance(), "Home");
                        break;

                    case R.id.d_menu_donwload:
                        replaceFrag(FragDownload.getInstance(), "My Downloads");

                        break;

                    case R.id.d_menu_new_release:
                        replaceFrag(new FragNewRelease(), "New Release");

                        break;
                    case R.id.d_menu_album:
                        replaceFrag(new FragAlbum(), "Albums");
                        break;

                    case R.id.d_menu_artist:
                        replaceFrag(new FragArtistView(), "Artists");
                        break;

                    case R.id.d_menu_playlist:
                        replaceFrag(new FragMyPlaylist(), "My Playlist");
                        break;


                }
                return true;
            }
        });

        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelCollapsed(View panel) {
                songbarPlayButton.setVisibility(View.VISIBLE);
                imgEdit.setVisibility(View.GONE);
                btnSave.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
            }

            @Override
            public void onPanelExpanded(View panel) {
                songbarPlayButton.setVisibility(View.GONE);
                imgEdit.setVisibility(View.VISIBLE);
                if (onUpdateUI != null)
                    onUpdateUI.updateUI(imgEdit, btnSave, btnCancel);

            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {


            }
        });


        prepareBottomPlayer(runningSong);

        songbarPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MusicService.class);
                intent.setAction(MusicService.PLAY_ACTION);
                startService(intent);
            }
        });

        replaceFrag(FragHomePage.getInstance(), "Home");
    }


    public void setOnUpdateUI(OnUpdateUI onUpdateUI) {
        this.onUpdateUI = onUpdateUI;
    }


    @Override
    public void onBackPressed() {


        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
            if (titles != null && titles.size() > 1) {
                titles.remove(titles.size() - 1);
                setActionbarTitle(titles.get(titles.size() - 1));

            }
        }


    }



    public boolean isStoragePermissionGranted() {
        String granted = CommonFunc.getPref(context, "isGranted");
        if (granted != null && granted.equals("true")) {
            Globals.isStoragePerGranted = true;
            return true;
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            CommonFunc.savePref(context,"isGranted","true");
            Globals.isStoragePerGranted=true;
            return true;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Globals.isStoragePerGranted = true;
            CommonFunc.savePref(context, "isGranted", "true");
            //resume tasks needing this permission
        }
    }



    public void prepareBottomPlayer(MoSong _runningSong) {
        Log.e("MAIN", "call m");
        runningSong=_runningSong;
        if (runningSong == null)
            return;
        tvSongTitle.setText(runningSong.getTitle());
        tvSongAlbum.setText(runningSong.getAlbum_name());
        songbarPlayButton.setEnabled(true);
        switch (MusicService.playerState){
            case MusicService.STATE_LOADING:
                songbarPlayButton.setBackgroundResource(R.drawable.loading);
                break;
            case MusicService.STATE_PAUSE:
                songbarPlayButton.setBackgroundResource(R.drawable.play_icon);
                break;
            case MusicService.STATE_PLAYING:
                songbarPlayButton.setBackgroundResource(R.drawable.pause_icon);
                break;
            case MusicService.STATE_NOT_READY:
                songbarPlayButton.setBackgroundResource(R.drawable.loading);
                break;
        }





        if (runningSong != null && runningSongID != runningSong.getId()) {
            runningSongID=runningSong.getId();
            final String imgUrl = runningSong.getImgUrl().isEmpty() ? Urls.BASE_URL + Urls.IMG_SONG + "6e83e5d5fee89ad93c147322a1314076.jpg" : /*Urls.BASE_URL + Urls.IMG_SONG + */runningSong.getImgUrl();
            Log.e("PIC", imgUrl);

            Picasso.with(context)
                    .load(imgUrl)
                    .placeholder(R.drawable.sync_icon)
                    .into(imageView_SongBar);

        }


    }



    public void replaceFrag(Fragment fragment, String title) {
        fm.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        setActionbarTitle(title);
        titles.add(title);
    }

    private void setActionbarTitle(String title) {
        getSupportActionBar().setTitle(title);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private void startMusicService() {
        Intent startIntent = new Intent(this, MusicService.class);
        startIntent.setAction(MusicService.NORMAL_ACTION);
        startService(startIntent);
        bindService(startIntent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    private void syncSongInfo() {
        DbManager db = new DbManager(this);
        final ArrayList<MoSongCount> songCounts;
        songCounts = db.getSongsCount();
        db.close();

        if (InternetConnectivity.isConnectedToInternet(this)) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();

            JSONObject j = new JSONObject();
            try {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < songCounts.size(); i++) {
                    jsonObject = new JSONObject();
                    jsonObject.put("song_id", songCounts.get(i).getSongId());
                    jsonObject.put("user_app_id", "124");
                    jsonObject.put("action", songCounts.get(i).getAction());
                    jsonObject.put("sync_time", songCounts.get(i).getLastMod());

                    jsonArray.put(jsonObject);
                }
                j.put("song_list", jsonArray);
                //Log.e("JSON", j.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.put("song_list", j.toString());
            client.post("http://52.89.156.64/index.php/api/songs_count", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.e("JSON______", response.toString());

                    ArrayList<MoSongCount> ss = new ArrayList<MoSongCount>();
                    for (int i = 0; i < songCounts.size(); i++) {
                        MoSongCount s = new MoSongCount();
                        s.setSongId(songCounts.get(i).getSongId());
                        s.setStatus(1);
                        ss.add(s);
                    }
                    DbManager db = new DbManager(MainActivity.this);
                    db.updateSongsCountList(ss);
                    db.close();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.e("JSON______", responseString);
                }
            });

        }
    }
}
