package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import baajna.scroll.owner.mobioapp.utils.Globals;
import com.mobioapp.baajna.R;
import baajna.scroll.owner.mobioapp.utils.Song;

import java.util.ArrayList;

/**
 * Created by LunaMac on 15. 3. 28..
 */
public class TitleAdapter extends BaseAdapter {
    //song list and layout
    private ArrayList<Song> songs;
    private LayoutInflater songInf;

    //constructor
    public TitleAdapter(Context c, ArrayList<Song> theSongs) {
        songInf = LayoutInflater.from(c);
        songs = theSongs;
    }

    public int getCount() {
        return songs.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        //map to song layout
        RelativeLayout songLay = (RelativeLayout) songInf.inflate
                (R.layout.song, parent, false);

        //get title and artist views
        ImageView albumArtView = (ImageView) songLay.findViewById(R.id.img_cover);
        TextView songView = (TextView) songLay.findViewById(R.id.tv_song_title);
        //TextView artistView = (TextView) songLay.findViewById(R.id.song_artist);

        //get title and artist strings
        String albumArtUri = Globals.titlelist.get(position).getImgUrl();


        songView.setText(Globals.titlelist.get(position).getTitle());
        //artistView.setText(Globals.titlelist.get(position).getTitle());
        //set position as tag
        songLay.setTag(position);
        return songLay;
    }
}



