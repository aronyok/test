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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumListAdapter extends BaseAdapter {

    private ArrayList<MoAlbum> albumList;
    private LayoutInflater mInflater;
    private Context mContext;


    public AlbumListAdapter(Context context, ArrayList<MoAlbum> albumList) {
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
            convertView = mInflater.inflate(R.layout.album, null);
            holder = new ViewHolder();
            holder.imgViewCoverPic = (ImageView) convertView.findViewById(R.id.img_view_cover_pic);
            holder.txtViewAlbumName = (TextView) convertView.findViewById(R.id.txt_view_name);
            holder.txtViewAlbumDetails = (TextView) convertView.findViewById(R.id.txt_view_details);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewAlbumName.setText(albumList.get(position).getTitle());
        //holder.txtViewAlbumDetails.setText(albumList.get(position).getAlbumDetails());

        Picasso.with(mContext)
                .load(albumList.get(position).getImgUrl())
                .placeholder(R.drawable.main_myplaylist)
                .into(holder.imgViewCoverPic);


        return convertView;
    }

    static class ViewHolder {
        ImageView imgViewCoverPic;
        TextView txtViewAlbumName;
        TextView txtViewAlbumDetails;
    }

}