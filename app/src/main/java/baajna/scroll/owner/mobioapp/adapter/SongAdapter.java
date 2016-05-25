package baajna.scroll.owner.mobioapp.adapter;

/**
 * Created by Lee on 2015-03-04.
 * Updated by Jewel on 2016-01-15
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.utils.Urls;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.services.MusicService;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter {

    //song list and layout
    private ArrayList<MoSong> songs;
    private LayoutInflater songInf;
    private Context context;
    private RelativeLayout songLay;

    //constructor
    public SongAdapter(Context context, ArrayList<MoSong> theSongs) {
        this.context = context;
        songs = theSongs;
        songInf = LayoutInflater.from(context);
    }

    public void setSongs(ArrayList<MoSong> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //map to song layout
        RelativeLayout songLay = (RelativeLayout) songInf.inflate
                (R.layout.song, parent, false);

        //get title and artist views
        ImageView albumArtView = (ImageView) songLay.findViewById(R.id.img_cover);
        TextView songView = (TextView) songLay.findViewById(R.id.tv_song_title);
        //TextView artistView = (TextView) songLay.findViewById(R.id.song_artist);
        //get song using position
        MoSong currSong = songs.get(position);


        songView.setText(currSong.getTitle());
        //artistView.setText(currSong.getArtistId());

        String imgUrl = currSong.getImgUrl().isEmpty() ? Urls.BASE_URL + Urls.IMG_SONG + "6e83e5d5fee89ad93c147322a1314076.jpg" : Urls.BASE_URL + Urls.IMG_SONG + currSong.getImgUrl();
        if (MusicService.playerState != 1) {
            if (MusicService.songPosn == position)
                albumArtView.setBackgroundResource(R.drawable.play_icon);
            else
                Picasso.with(context)
                        .load(imgUrl)
                        .placeholder(R.drawable.sync_icon)
                        .into(albumArtView);
        } else {

        }

        //set position as tag
        songLay.setTag(position);
        songLay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MusicService.playSong(position);


            }
        });
        return songLay;
    }


}
