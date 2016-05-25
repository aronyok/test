package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.datamodel.MoPlayList;
import baajna.scroll.owner.mobioapp.utils.MyApp;
import baajna.scroll.owner.mobioapp.utils.Urls;
import baajna.scroll.owner.mobioapp.activity.SingleSongActivity;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.services.MusicService;
import baajna.scroll.owner.mobioapp.utils.Globals;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jewel on 1/18/2016.
 */
public class AdDownload extends RecyclerView.Adapter<AdDownload.MyViewHolder> {

    private ArrayList<MoSong> songs;
    private LayoutInflater inflater;
    private Context context;
    private boolean isEditable;

    public AdDownload(Context context) {
        songs = new ArrayList<>();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<MoSong> albumList,boolean isEditable) {
        this.songs = albumList;
        this.isEditable=isEditable;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_song_list_download, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MoSong song = new MoSong();
        song = songs.get(position);
        holder.tvTitle.setText(song.getTitle());

        String imgUrl = song.getImgUrl().isEmpty() ? "" : Urls.BASE_URL + Urls.IMG_ALBUM + song.getImgUrl();
        Picasso.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.sync_icon)
                .into(holder.imgAlbum);

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgAlbum,imgSongDetails,imgDelete;
        public TextView tvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgAlbum = (ImageView) itemView.findViewById(R.id.img_song_album_cover);
            imgDelete= (ImageView) itemView.findViewById(R.id.img_song_delete);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_song_title);
            imgSongDetails = (ImageView) itemView.findViewById(R.id.img_song_detail_view_icon);

            itemView.findViewById(R.id.img_song_download_icon).setVisibility(View.GONE);

            if(isEditable){
                imgSongDetails.setVisibility(View.GONE);
                imgDelete.setVisibility(View.VISIBLE);
            }else{
                imgSongDetails.setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.img_song_delete).setVisibility(View.GONE);
            }

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int songId=songs.get(getAdapterPosition()).getId();
                    if(Globals.deletedSongIds!=null)
                        Globals.deletedSongIds=new ArrayList<Integer>();
                    Globals.deletedSongIds.add(songId);
                    songs.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoSong downloadedSong = songs.get(getAdapterPosition());

                    DbManager mydb = new DbManager(MyApp.getAppContext());
                    MoPlayList playList = new MoPlayList();
                    //playList.setId(downloadedSong.ge);
                    playList.setSongId(downloadedSong.getId());

                    long result = mydb.addToPlaylist(playList);
                    MusicService.songs = mydb.getSongs(DbManager.SQL_SONGS_PLAYLIST_RUNNING);
                    mydb.close();

                    for (int i = 0; i < MusicService.songs.size(); i++) {
                        if (downloadedSong.getId() == MusicService.songs.get(i).getId()) {
                            MusicService.songPosn = i;
                            break;
                        }
                    }
                    //if (MusicService.isRunning) {
                    MusicService.playSong(MusicService.songPosn);


                }
            });

            imgSongDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoSong song = songs.get(getAdapterPosition());
                    //((MainActivity) context).replaceFrag(FragSingleSong.getInstance(song.getId()), song.getTitle());
                    context.startActivity(new Intent(context, SingleSongActivity.class).putExtra("song_id", song.getId()));
                }
            });


        }
    }
}
