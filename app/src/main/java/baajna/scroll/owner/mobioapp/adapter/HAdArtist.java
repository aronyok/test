package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.fragment.FragArtistSingleView;
import baajna.scroll.owner.mobioapp.fragment.FragArtistView;
import baajna.scroll.owner.mobioapp.utils.Urls;
import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.datamodel.MoArtist;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sajal on 1/26/2016.
 */
public class HAdArtist extends ArrayAdapter<MoArtist> {
    int resource;
    private Context context;
    private LayoutInflater inflater = null;
    private ArrayList<MoArtist> artists;
    //private MoAlbum albums;


    public HAdArtist(Context context, int resource, ArrayList<MoArtist> artists) {

        super(context, resource, artists);
        this.context = context;
        this.artists = artists;
        inflater = LayoutInflater.from(this.context);

    }

    public void setData(ArrayList<MoArtist> artistList){
        this.artists=artistList;
        notifyDataSetChanged();
    }

    public void setFilter(ArrayList<MoArtist> artistList) {
        artists = new ArrayList<>();
        artists.addAll(artistList);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder holder;

        if (view == null) {

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.row_artist, null);
            holder = new ViewHolder();
            holder.tv_album_title = (TextView) view
                    .findViewById(R.id.tv_new_release_title);
            holder.tv_album_artist = (TextView) view.findViewById(R.id.tv_new_release_artist);

            holder.img_album_cover = (ImageView) view.findViewById(R.id.img_new_albums);
            holder.lay_artists = (LinearLayout) view.findViewById(R.id.lay_artist_home);
            //convertView=inflater.inflate(R.layout.row_home,parent,false);

            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MoArtist artist = artists.get(position);

                int type=artist.getId();
                if(type==0){
                    ((MainActivity) getContext()).replaceFrag(FragArtistView.getInstance(), "All Artists");
                }else

                    ((MainActivity) getContext()).replaceFrag(FragArtistSingleView.getInstance(artist.getId(), 1), artist.getName());

            }
        });

        //ImageView imgNewAlbum= (ImageView) convertView.findViewById(R.id.img_new_albums);
        //TextView tvAlbumName= (TextView) convertView.findViewById(R.id.tv_new_release_title);

        holder.tv_album_title.setText(artists.get(position).getName());
        MoArtist artist = new MoArtist();
        artist = artists.get(position);
        /*String imgUrl = album.getImgUrl().isEmpty() ? Urls.BASE_URL + Urls.IMG_ALBUM + "6e83e5d5fee89ad93c147322a1314076.jpg" : Urls.BASE_URL + Urls.IMG_ALBUM + album.getImgUrl();
        Picasso.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.sync_icon)
                .into(holder.img_album_cover);
*/

        if(artist.getId()==0){
            holder.img_album_cover.setImageResource(R.drawable.view_all_button);
            holder.tv_album_title.setText("View all");
        }
        else{
            String imgUrl = artist.getImge().isEmpty() ? Urls.BASE_URL + Urls.IMG_ARTIST + "6e83e5d5fee89ad93c147322a1314076.jpg" : Urls.BASE_URL + Urls.IMG_ARTIST + artist.getImge();
            Picasso.with(context)
                    .load(imgUrl)
                    .placeholder(R.drawable.sync_icon)
                    .into(holder.img_album_cover);
        }

        return view;



    }

    /*********
     * Create a holder Class to contain inflated xml file elements
     *********/
    public class ViewHolder {

        public LinearLayout lay_artists;
        public TextView tv_album_title;
        public TextView tv_album_artist;
        public ImageView img_album_cover;


    }
}
