package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.activity.SingleSongActivity;
import baajna.scroll.owner.mobioapp.connection.InternetConnectivity;
import baajna.scroll.owner.mobioapp.datamodel.MoPlayList;
import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.services.MusicService;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sajal on 1/12/2016.
 */
public class AdExpandableList extends BaseExpandableListAdapter {


    private Context _context;
    private List<MoSong> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild; //child data in format of header title
    private LayoutInflater infalInflater;

    public AdExpandableList(Context context, List<MoSong> listDataHeader, HashMap<String, List<String>> listDataChild) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listDataChild;

        infalInflater=LayoutInflater.from(_context);
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return 1;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {



        if (convertView == null) {
            convertView = infalInflater.inflate(R.layout.row_child_list_song, null);
        }

        Button buttonSongPlay = (Button) convertView.findViewById(R.id.tv_play_song);
        Button buttonSongQueue = (Button) convertView.findViewById(R.id.tv_queue_song);
        Button buttonSongLike = (Button) convertView.findViewById(R.id.tv_like_song);

        buttonSongPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DbManager mydb = new DbManager(_context);
                MoPlayList playList = new MoPlayList();
                playList.setId(0);
                playList.setSongId(_listDataHeader.get(groupPosition).getId());
                long result = mydb.addToPlaylist(playList);
                MusicService.songs = mydb.getSongs(DbManager.SQL_SONGS_PLAYLIST_RUNNING);
                MusicService.songPosn = MusicService.songs.size() - 1;
                mydb.close();
               // songs = MusicService.songs;
                for (int i = 0; i < MusicService.songs.size(); i++) {
                    if (playList.getSongId() == MusicService.songs.get(i).getId()) {
                        MusicService.songPosn = i;
                        break;
                    }
                }
                if (MusicService.isRunning) {
                    MusicService.playSong(MusicService.songPosn);
                } else {
                    /*Intent playIntent = new Intent(_context, MusicPlayerActivity.class);
                    playIntent.putExtra("ParentClassName", "SongListActivity");
                    playIntent.putExtra("song_index", groupPosition);
                    //Globals.songNumber = selectedPos;
                    _context.startActivity(playIntent);*/


                    startMusicService();
                }


            }
        });

        buttonSongQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DbManager mydb = new DbManager(_context);
                MoPlayList playList = new MoPlayList();
                playList.setId(0);
                playList.setSongId(_listDataHeader.get(groupPosition).getId());
                long result = mydb.addToPlaylist(playList);
                MusicService.songs = mydb.getSongs(DbManager.SQL_SONGS_PLAYLIST_RUNNING);
                mydb.close();
                Toast.makeText(_context, "Added player Queue", Toast.LENGTH_LONG).show();


            }
        });
        buttonSongLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }


    @Override
    public MoSong getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }


    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String headerTitle = getGroup(groupPosition).getTitle();
        final MoSong selectedSong = _listDataHeader.get(groupPosition);
        if (convertView == null) {
            convertView = infalInflater.inflate(R.layout.row_songlist_album_view, parent,false);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.tv_song_title);
        ((TextView) convertView.findViewById(R.id.tv_song_serial)).setText(groupPosition+1+"");
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        //ImageView imgSongDetails = (ImageView) convertView.findViewById(R.id.img_song_details);
        LinearLayout laySongDetails= (LinearLayout) convertView.findViewById(R.id.lay_song_details);


        laySongDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoSong song=_listDataHeader.get(groupPosition);
                //((MainActivity)_context).replaceFrag(FragSingleSong.getInstance(song.getId()), song.getTitle());
                _context.startActivity(new Intent(_context, SingleSongActivity.class).putExtra("song_id", song.getId()));
            }
        });

        final ImageView imgSongDownload = (ImageView) convertView.findViewById(R.id.img_song_download);
        final LinearLayout laySongDownload= (LinearLayout) convertView.findViewById(R.id.lay_song_download);
        laySongDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v == laySongDownload) {
                    imgSongDownload.setImageResource(R.drawable.download_512_active);
                }
                if (InternetConnectivity.isConnectedToInternet(_context)) {
                    CommunicationLayer.getInstance().getDownloadFile("1", selectedSong.getId() + "");

                }

            }
        });

        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void startMusicService() {
        Intent startIntent = new Intent(_context, MusicService.class);
        startIntent.setAction(MusicService.MAIN_ACTION);
        _context.startService(startIntent);
    }
}