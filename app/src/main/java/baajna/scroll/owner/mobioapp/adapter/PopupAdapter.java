package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;

import java.util.ArrayList;

public class PopupAdapter extends BaseAdapter {

    private ArrayList<MoAlbum> albumList;
    private LayoutInflater mInflater;
    private Context mContext;


    public PopupAdapter(Context context, ArrayList<MoAlbum> albumList) {
        this.mContext = context;
        this.albumList = albumList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.playlist_add_song, null);
            holder = new ViewHolder();
            holder.imgViewCoverPic = (ImageView) convertView.findViewById(R.id.playlist_add_imageview);
            holder.txtViewAlbumName = (TextView) convertView.findViewById(R.id.playlist_add_view_name);
            //holder.txtViewAlbumDetails = (TextView) convertView.findViewById(R.id.playlist_add_view_details);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       /* holder.txtViewAlbumName.setText(albumList.get(position).getAlbumName());
        //holder.txtViewAlbumDetails.setText(albumList.get(position).getAlbumDetails());

        Ion.with(mContext)
                .load(albumList.get(position).getImge())
                .withBitmap()
                .placeholder(R.mipmap.sparkle_icon72)
                .error(R.mipmap.sparkle_icon72)
                .intoImageView(holder.imgViewCoverPic);*/

        return convertView;
    }

    static class ViewHolder {
        ImageView imgViewCoverPic;
        TextView txtViewAlbumName;
        TextView txtViewAlbumDetails;
    }

}