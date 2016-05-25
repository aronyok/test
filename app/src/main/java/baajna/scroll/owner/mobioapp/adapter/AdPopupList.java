package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.datamodel.MoPlayList;

import java.util.ArrayList;

/**
 * Created by Jewel on 1/4/2016.
 */
public abstract class AdPopupList extends RecyclerView.Adapter<AdPopupList.MyViewHolder> {

    private ArrayList<MoPlayList> albumList;
    private LayoutInflater inflater;
    private Context context;
    private View view;

    public AdPopupList(Context context){
        albumList=new ArrayList<>();
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    public void setData(ArrayList<MoPlayList> albumList){
        this.albumList=albumList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.row_popup_list,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MoPlayList artist=new MoPlayList();
        artist=albumList.get(position);
        holder.tvTitle.setText(artist.getName());
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgCover;
        public TextView tvTitle;
        public MyViewHolder(View itemView) {
            super(itemView);
            imgCover=(ImageView)itemView.findViewById(R.id.img_cover);
            tvTitle=(TextView)itemView.findViewById(R.id.tv_popup_list_name);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAfterClick(getAdapterPosition());

                }
            });

        }
    }
    public abstract void onAfterClick(int position);
}
