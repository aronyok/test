package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;
import baajna.scroll.owner.mobioapp.utils.Urls;
import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.fragment.FragExpandable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jewel on 1/4/2016.
 */
public abstract class AdAlbumSearch extends RecyclerView.Adapter<AdAlbumSearch.MyViewHolder> {

    private ArrayList<MoAlbum> albumList;
    private LayoutInflater inflater;
    private Context context;
    private  View view=null;

    public AdAlbumSearch(Context context){
        albumList=new ArrayList<>();
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    public void setData(ArrayList<MoAlbum> albumList){
        this.albumList=albumList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.row_search,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MoAlbum album=new MoAlbum();
        album=albumList.get(position);
        holder.tvTitle.setText(album.getTitle());
        //holder.tvAlbum.setText(album.getName());
        String imgUrl= album.getImgUrl().isEmpty()? Urls.BASE_URL+Urls.IMG_ALBUM +"6e83e5d5fee89ad93c147322a1314076.jpg":Urls.BASE_URL+Urls.IMG_ALBUM +album.getImgUrl();

        Picasso.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.sync_icon)
                .into(holder.imgCover);

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void setFilter(ArrayList<MoAlbum> albums) {
        albumList = new ArrayList<>();
        albumList.addAll(albums);
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


           /* WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            int width=(display.getWidth())/2-20;


            ViewGroup.LayoutParams params=imgCover.getLayoutParams();
            params.width= width;
            params.height=width;*/

            imgOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new Utils().showPopup(v,context);
                    onAfterClick(v,getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoAlbum album=albumList.get(getAdapterPosition());

                    ((MainActivity)context).replaceFrag(FragExpandable.getInstance(album.getId(),1),album.getTitle());

                }
            });

        }
    }
    public abstract void onAfterClick(View view,int position);
}
