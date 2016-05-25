package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;
import baajna.scroll.owner.mobioapp.fragment.FragExpandable;
import baajna.scroll.owner.mobioapp.utils.Urls;
import baajna.scroll.owner.mobioapp.activity.MainActivity;
import baajna.scroll.owner.mobioapp.fragment.FragAlbum;

import com.mobioapp.baajna.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sajal on 1/26/2016.
 */
public class HAdAlbum extends ArrayAdapter<MoAlbum> {
    int resource;
    private Context context;
    private LayoutInflater inflater = null;
    private ArrayList<MoAlbum> albums;



    public HAdAlbum(Context context, int resource, ArrayList<MoAlbum> albums) {

        super(context, resource, albums);
        this.context = context;
        this.albums = albums;
        inflater = LayoutInflater.from(this.context);

    }
    public void setData(ArrayList<MoAlbum> albumList){
        this.albums=albumList;
        notifyDataSetChanged();
    }

    public void setFilter(ArrayList<MoAlbum> albumList) {
        albums = new ArrayList<>();
        albums.addAll(albumList);
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder holder;

        if (view == null) {

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.row_home, null);
            holder = new ViewHolder();
            holder.tv_album_title = (TextView) view
                    .findViewById(R.id.tv_new_release_title);
            holder.tv_album_artist = (TextView) view.findViewById(R.id.tv_new_release_artist);

            holder.img_album_cover = (ImageView) view.findViewById(R.id.img_new_albums);
            holder.lay_albums = (LinearLayout) view.findViewById(R.id.lay_album_home);
            //convertView=inflater.inflate(R.layout.row_home,parent,false);

            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MoAlbum album = albums.get(position);
                //Intent intent = new Intent(context, ExpandableListActivity.class);
                //intent.putExtra("album_id", album.getId());
                //intent.putExtra("album_name", album.getTitle());
                //intent.putExtra("isArtist", album.getArtistName());
                //context.startActivity(intent);
                //((MainActivity)context).replaceFrag(FragExpandable.getInstance(album.getId(),1),album.getTitle());
                int type=album.getId();
                if(type==0){
                    ((MainActivity) getContext()).replaceFrag(FragAlbum.getInstance(), "Albums");
                }else
                    ((MainActivity)context).replaceFrag(FragExpandable.getInstance(album.getId(), 1),album.getTitle());

            }
        });

        //ImageView imgNewAlbum= (ImageView) convertView.findViewById(R.id.img_new_albums);
        //TextView tvAlbumName= (TextView) convertView.findViewById(R.id.tv_new_release_title);

        holder.tv_album_title.setText(albums.get(position).getTitle());
        MoAlbum album = new MoAlbum();
        album = albums.get(position);
        /*String imgUrl = album.getImgUrl().isEmpty() ? Urls.BASE_URL + Urls.IMG_ALBUM + "6e83e5d5fee89ad93c147322a1314076.jpg" : Urls.BASE_URL + Urls.IMG_ALBUM + album.getImgUrl();
        Picasso.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.sync_icon)
                .into(holder.img_album_cover);
*/

        if(album.getId()==0){
            holder.img_album_cover.setImageResource(R.drawable.view_all_button);
            holder.tv_album_title.setText("View all");
            //holder.tv_album_title.setGravity();
        }
        else{
            String imgUrl = album.getImgUrl().isEmpty() ? Urls.BASE_URL + Urls.IMG_ALBUM + "6e83e5d5fee89ad93c147322a1314076.jpg" : Urls.BASE_URL + Urls.IMG_ALBUM + album.getImgUrl();
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

        public LinearLayout lay_albums;
        public TextView tv_album_title;
        public TextView tv_album_artist;
        public ImageView img_album_cover;


    }
}
