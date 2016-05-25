package baajna.scroll.owner.mobioapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobioapp.baajna.R;

/**
 * Created by rohan on 9/30/15.
 */
public class Navigationdrawer extends BaseAdapter {


    String[] result;
    Context context;
    int[] imageId;
    private static LayoutInflater inflater = null;

    public Navigationdrawer(Context context, String[] result, int[] imageId) {
        this.result = result;
        this.context = context;
        this.imageId = imageId;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.drawer_xml, null);
        holder.tv = (TextView) rowView.findViewById(R.id.label);
        holder.img = (ImageView) rowView.findViewById(R.id.drawer_imageview);
        holder.tv.setText(result[position]);

        holder.img.setImageResource(imageId[position]);

        return rowView;
    }
}
