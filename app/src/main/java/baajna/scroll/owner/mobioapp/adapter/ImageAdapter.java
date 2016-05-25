package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import baajna.scroll.owner.mobioapp.utils.Globals;
import com.mobioapp.baajna.R;

/**
 * Created by Luna on 2015-04-29.
 */
public class ImageAdapter extends BaseAdapter {

    private LayoutInflater inflater;


    public ImageAdapter(Context c) {
        inflater = LayoutInflater.from(c);
    }

    public ImageAdapter() {

    }

    @Override
    public int getCount() {
        return Globals.playlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.song, parent, false);
            holder = new ViewHolder();
            holder.textTitle = (TextView) view.findViewById(R.id.tv_song_title);
            //holder.textArtist = (TextView) view.findViewById(R.id.song_artist);
            holder.imageAlbumArt = (ImageView) view.findViewById(R.id.img_cover);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //holder.textTitle.setText(Globals.playlist.get(Globals.songNumber).getTitle());
        //holder.textArtist.setText(Globals.playlist.get(Globals.songNumber).getArtistId());

        //String albumArtUri = Globals.playlist.get(Globals.songNumber).getImgUrl();



     /*   if (albumArtUri != null) {
            ImageLoader.getInstance().displayImage(albumArtUri, holder.imageAlbumArt, options, animateFirstListener);
        } else {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.image_noimage, holder.imageAlbumArt, options, new SimpleImageLoadingListener());
        }
*/
        return view;
    }


    private static class ViewHolder {
        TextView textTitle;
        TextView textArtist;
        ImageView imageAlbumArt;
    }
}


