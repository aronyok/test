package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.activity.SingleSongActivity;
import baajna.scroll.owner.mobioapp.utils.Globals;
import baajna.scroll.owner.mobioapp.utils.Urls;
import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.services.MusicService;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jewel on 2/2/2016.
 */
public class AdMusicPlayer extends RecyclerView.Adapter<AdMusicPlayer.MyViewHolder> {
    private ArrayList<MoSong> songs;
    private LayoutInflater inflater;
    private Context context;
    private View view;
    private boolean isEditable;

    public AdMusicPlayer(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        songs = new ArrayList<>();

    }

    public void setSongs(ArrayList<MoSong> songs,boolean isEditable) {
        this.songs = songs;
        this.isEditable=isEditable;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_song_list_music_player, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MoSong currSong = songs.get(position);
        holder.tvTitle.setText(currSong.getTitle());
        holder.tvAlbum.setText(currSong.getAlbum_name());
        //holder.imgDownload.setImageResource(currSong.getIsDownloaded()==1);?:;
        holder.imgDownload.setVisibility(currSong.getIsDownloaded() == 1 ? View.INVISIBLE : View.VISIBLE);
        String imgUrl = TextUtils.isEmpty(currSong.getImgUrl()) ? Urls.BASE_URL + Urls.IMG_SONG + "6e83e5d5fee89ad93c147322a1314076.jpg" : Urls.BASE_URL + Urls.IMG_SONG + currSong.getImgUrl();
        if (MusicService.songPosn == position)
            holder.img.setImageResource(R.drawable.playing_icon);
        else
            Picasso.with(context)
                    .load(imgUrl)
                    .placeholder(R.drawable.sync_icon)
                    .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout lay_Song_Details;
        public ImageView img, imgDownload, imgDetails,imgDelete;
        public TextView tvTitle, tvAlbum;

        public MyViewHolder(View itemView) {
            super(itemView);
            lay_Song_Details= (LinearLayout) itemView.findViewById(R.id.lay_song_details_option);
            img = (ImageView) itemView.findViewById(R.id.img_song_album_cover);
            imgDetails = (ImageView) itemView.findViewById(R.id.img_song_detail_view_icon);
            imgDelete = (ImageView) itemView.findViewById(R.id.img_song_delete);
            imgDownload = (ImageView) itemView.findViewById(R.id.img_song_download_icon);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_song_title);
            tvAlbum = (TextView) itemView.findViewById(R.id.tv_song_album);

            if(isEditable){
                imgDetails.setVisibility(View.GONE);
                imgDownload.setVisibility(View.GONE);
                imgDelete.setVisibility(View.VISIBLE);
            }else{
                imgDetails.setVisibility(View.VISIBLE);
                imgDownload.setVisibility(View.VISIBLE);
                imgDelete.setVisibility(View.GONE);
            }



            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int songId=songs.get(getAdapterPosition()).getId();
                    Log.d("Jewel",songId+" deleted id");
                    Globals.deletedSongIds.add(songId);
                    songs.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
            lay_Song_Details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoSong song = songs.get(getAdapterPosition());
                    //((MainActivity) context).replaceFrag(FragSingleSong.getInstance(song.getId()), song.getTitle());
                    context.startActivity(new Intent(context, SingleSongActivity.class).putExtra("song_id", song.getId()));
                    ((MainActivity) context).mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MusicService.playSong(getAdapterPosition());


                }
            });
        }
    }

}
