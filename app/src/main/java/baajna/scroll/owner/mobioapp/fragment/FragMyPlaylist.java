package baajna.scroll.owner.mobioapp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.adapter.AdPlaylist;
import baajna.scroll.owner.mobioapp.datamodel.MoPlayList;
import baajna.scroll.owner.mobioapp.utils.MyApp;
import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.utils.Globals;
import baajna.scroll.owner.mobioapp.utils.SpacesItemDecoration;

import java.util.ArrayList;

/**
 * Created by Jewel on 1/25/2016.
 */
public class FragMyPlaylist extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<MoPlayList>playList;
    private AdPlaylist adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_recycler,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter=new AdPlaylist(getContext()) {
            @Override
            public void onAfterClick(View view, int position) {
                ((MainActivity)getContext()).replaceFrag(FragExpandableList.getInstance(playList.get(position).getId(), Globals.TYPE_EXP_PLAYLIST), playList.get(position).getName());
            }
        };
        recyclerView.setAdapter(adapter);
        prepareDis();

    }
    private void prepareDis(){
        DbManager db=new DbManager(getContext());
        playList=db.getPlayListName();
        adapter.setData(playList);
        db.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu.add(Menu.NONE, R.id.action_search, Menu.NONE, "Add");
        inflater.inflate(R.menu.menu_myplaylist,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showDialog(){
        final Dialog mDialog = new Dialog(getContext());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.popup_create_playlist);
        Button btnSave = (Button) mDialog.findViewById(R.id.btn_save);
        Button btnCancel = (Button) mDialog.findViewById(R.id.btn_close_popup);

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
                    prepareDis();
                } else {
                    Toast.makeText(getContext(), "Empty name !!", Toast.LENGTH_LONG).show();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }
}
