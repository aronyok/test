package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import baajna.scroll.owner.mobioapp.utils.Artist;
import com.mobioapp.baajna.R;

import java.util.ArrayList;

/**
 * Created by LunaMac on 15. 3. 26..
 */
public class ArtistAdapter extends BaseAdapter {
    //song list and layout
    private ArrayList<Artist> artists;
    private LayoutInflater songInf;

    //constructor
    public ArtistAdapter(Context c, ArrayList<Artist> theArtists) {
        songInf = LayoutInflater.from(c);
        artists = theArtists;
    }

    public int getCount() {
        return artists.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //map to song layout
        RelativeLayout songLay = (RelativeLayout) songInf.inflate(R.layout.artist, parent, false);

        //get title and artist views
        ImageView albumImageView = (ImageView) songLay.findViewById(R.id.artist_image);
        TextView artistView = (TextView) songLay.findViewById(R.id.artist_name);

        String albumArtUri = artists.get(position).getAlbumArtUri();



        String artistName = artists.get(position).getArtist();
        if (artistName.length() >= 14) {
            artistName = artistName.substring(0, 13) + "...";
        }
        artistView.setText(artistName);

        //set position as tag
        songLay.setTag(position);
        return songLay;
    }
}