package baajna.scroll.owner.mobioapp.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.adapter.AdDownload;
import baajna.scroll.owner.mobioapp.services.MusicService;
import baajna.scroll.owner.mobioapp.utils.MyApp;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.utils.Globals;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jewel on 1/18/2016.
 */
public class FragDownload extends Fragment {
    private RecyclerView recyclerView;
    private AdDownload adapter;
    private ArrayList<MoSong> songs;
    private DbManager db;
    private View view = null;
    private Context context;
    private LayoutInflater inflater = null;
    boolean isChecked;

    private static FragDownload fragDownload;

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



    public static FragDownload getInstance() {

        fragDownload = new FragDownload();
        return fragDownload;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_recycler, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent=new Intent(getContext(),MusicService.class);
        intent.setAction(MusicService.NORMAL_ACTION);
        getContext().startService(intent);
        getContext().bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unbindService(serviceConnection);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        context = getContext();
        inflater = LayoutInflater.from(context);
        songs = new ArrayList<>();
        db = new DbManager(MyApp.getAppContext());
        songs = db.getSongs(DbManager.SQL_SONGS_DOWNLOAD);
        db.close();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter =new AdDownload(context,musicService) ;
        adapter.setData(songs, false);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_download, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_edit) {
            isChecked = !isChecked;
            if (isChecked) {
                item.setIcon(R.drawable.save);

            } else {
                item.setIcon(R.drawable.menu_edit);
                if (Globals.deletedSongIds.size() > 0) {
                    db = new DbManager(context);
                    for (int i = 0; i < Globals.deletedSongIds.size(); i++){
                        long result=db.removeDownload(Globals.deletedSongIds.get(i));


                        if(result>0){
                            String fileName=db.getSong(Globals.deletedSongIds.get(i)).getFileName();
                            String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
                            File file=new File(dir+ Globals.DOWNLOAD_FOLDER+fileName);
                            if(file.exists()){
                                file.delete();
                                Log.d("Jewel","Got file:"+fileName);
                            }
                        }
                    }

                    db.close();
                }

            }
            adapter.setData(songs, isChecked);
            recyclerView.setAdapter(adapter);

        }
        return super.onOptionsItemSelected(item);

    }
}
