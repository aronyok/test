package baajna.scroll.owner.mobioapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mobioapp.baajna.R;
/**
 * Created by Jewel on 1/25/2016.
 */
public class Utils {
    public void showPopup(View anchorView,Context context) {

        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_layout, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);



        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                location[0], location[1] + anchorView.getHeight());

        TextView tvPlayAll= (TextView) popupView.findViewById(R.id.tv_play_all);
        TextView tvAddQueue= (TextView) popupView.findViewById(R.id.tv_add_to_queue);
        TextView tvPlayList= (TextView) popupView.findViewById(R.id.tv_add_to_playlist);
        TextView tvDownload= (TextView) popupView.findViewById(R.id.tv_download);

        tvPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               

                popupWindow.dismiss();
            }
        });

    }

    public void saveSharedPref(String key,String value){
        SharedPreferences sp=MyApp.getAppContext().getSharedPreferences(Globals.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public String getSharedPref(String key){
        SharedPreferences sp=MyApp.getAppContext().getSharedPreferences(Globals.APP_NAME,Context.MODE_PRIVATE);
        if(!sp.contains(key))
            return "na";
        return sp.getString(key,"na");
    }



}
