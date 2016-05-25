package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.datamodel.MoSong;

import java.util.ArrayList;

/**
 * Created by rohan on 9/29/15.
 */
public class EmptyListView extends BaseAdapter {


    private ArrayList<MoSong> songList;
    private LayoutInflater mInflater;
    private Context mContext;


    public EmptyListView(Context context) {
        this.mContext = context;
        MoSong info = new MoSong();
        info.setId(0);
        songList.add(info);
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
            convertView = mInflater.inflate(R.layout.empty_listview, null);
            holder = new ViewHolder();

            holder.empty_Text = (TextView) convertView.findViewById(R.id.empty_Text);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }

    static class ViewHolder {

        TextView empty_Text;

        //ImageButton imageButtonSongMenu;
    }
}
