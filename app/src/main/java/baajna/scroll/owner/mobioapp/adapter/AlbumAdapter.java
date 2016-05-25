package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mobioapp.baajna.R;
import baajna.scroll.owner.mobioapp.utils.Album;

import java.util.ArrayList;

/**
 * Created by LunaMac on 15. 3. 26..
 */
public class AlbumAdapter extends BaseAdapter {
    //song list and layout
    private ArrayList<Album> albums;
    private LayoutInflater songInf;

    //constructor
    public AlbumAdapter(Context c, ArrayList<Album> theAlbums) {
        songInf = LayoutInflater.from(c);
        albums = theAlbums;
    }

    public int getCount() {
        return albums.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        //map to song layout
        RelativeLayout songLay = (RelativeLayout) songInf.inflate(R.layout.album, parent, false);

        //get title and artist views
        ImageView albumImageView = (ImageView) songLay.findViewById(R.id.img_view_cover_pic);
        TextView albumView = (TextView) songLay.findViewById(R.id.txt_view_name);
        TextView artistView = (TextView) songLay.findViewById(R.id.txt_view_details);

        //get title and artist strings

        String albumArtUri = albums.get(position).getAlbumArtUri();


        String albumTitle = albums.get(position).getAlbum();
        if (albumTitle.length() >= 14) {
            albumTitle = albumTitle.substring(0, 13) + "...";
        }
        albumView.setText(albumTitle);

        artistView.setText(albums.get(position).getArtist());

        //set position as tag
        songLay.setTag(position);
        return songLay;
    }
}
