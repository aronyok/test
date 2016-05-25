package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.datamodel.MoArtist;
import baajna.scroll.owner.mobioapp.fragment.FragArtistSingleView;
import baajna.scroll.owner.mobioapp.utils.Urls;

import com.mobioapp.baajna.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jewel on 1/4/2016.
 */
public class AdArtist extends RecyclerView.Adapter<AdArtist.MyViewHolder> {

    private ArrayList<MoArtist> albumList;
    private LayoutInflater inflater;
    private Context context;

    public AdArtist(Context context){
        albumList=new ArrayList<>();
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    public void setData(ArrayList<MoArtist> albumList){
        this.albumList=albumList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.row_artist_list,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MoArtist artist=new MoArtist();
        artist=albumList.get(position);
        holder.tvTitle.setText(artist.getName());

        //holder.tvAlbum.setText(artist.getName());
        String imgUrl= artist.getImge().isEmpty()? Urls.BASE_URL+Urls.IMG_ARTIST +"6e83e5d5fee89ad93c147322a1314076.jpg":Urls.BASE_URL+Urls.IMG_ARTIST +artist.getImge();
        Picasso.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.sync_icon)
                .into(holder.imgCover);

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void setFilter(ArrayList<MoArtist> artists) {
        albumList = new ArrayList<>();
        albumList.addAll(artists);
        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgCover,imgOption;
        public TextView tvTitle,tvArtist;
        public MyViewHolder(View itemView) {
            super(itemView);
            imgCover=(ImageView)itemView.findViewById(R.id.img_new_albums);
            imgOption=(ImageView)itemView.findViewById(R.id.img_new_release_options);

            tvTitle=(TextView)itemView.findViewById(R.id.tv_new_release_title);
            tvArtist=(TextView)itemView.findViewById(R.id.tv_new_release_artist);


            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            int width=(display.getWidth())/2-10;


            ViewGroup.LayoutParams params=imgCover.getLayoutParams();
            params.width= width;
            params.height=width;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoArtist artist=albumList.get(getAdapterPosition());


                    ((MainActivity)context).replaceFrag(FragArtistSingleView.getInstance(artist.getId(), 1),artist.getName());


                }
            });

        }
    }
}
