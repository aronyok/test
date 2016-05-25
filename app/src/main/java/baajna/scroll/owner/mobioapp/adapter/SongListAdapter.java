package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.utils.Urls;

import com.mobioapp.baajna.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongListAdapter extends BaseAdapter {

    private ArrayList<MoSong> songList;
    private LayoutInflater mInflater;
    private Context mContext;

    public SongListAdapter(Context context, ArrayList<MoSong> songList) {
        this.mContext = context;
        this.songList = songList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.song_list, null);
            holder = new ViewHolder();
            holder.imgViewCoverPic = (ImageView) convertView.findViewById(R.id.img_view_cover_pic);
            holder.txtViewSongName = (TextView) convertView.findViewById(R.id.txt_view_name);
            holder.txtViewSongDetails = (TextView) convertView.findViewById(R.id.txt_view_details);
            //holder.imageButtonSongMenu = (ImageButton) convertView.findViewById(R.id.imageButton_songMenu);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewSongName.setText(songList.get(position).getTitle());
        holder.txtViewSongDetails.setText(songList.get(position).getSongDetails());
        String imgUrl= songList.get(position).getImgUrl().isEmpty()? Urls.BASE_URL+Urls.IMG_ALBUM +"6e83e5d5fee89ad93c147322a1314076.jpg": Urls.BASE_URL+Urls.IMG_ALBUM +songList.get(position).getImgUrl();

        Picasso.with(mContext)
                .load(imgUrl)
                .placeholder(R.drawable.main_myplaylist)
                .into(holder.imgViewCoverPic);


        return convertView;
    }

    static class ViewHolder {
        ImageView imgViewCoverPic;
        TextView txtViewSongName;
        TextView txtViewSongDetails;
        //ImageButton imageButtonSongMenu;
    }


}