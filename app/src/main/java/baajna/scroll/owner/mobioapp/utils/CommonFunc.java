package baajna.scroll.owner.mobioapp.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jewel on 1/4/2016.
 */
public class CommonFunc {
    public static int getPixel(Context context,int pxl){
        float density=context.getResources().getDisplayMetrics().density;

        return (int)(pxl*density);
    }

    public static boolean isInternetOn(Context context) {

        try {
            ConnectivityManager con = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi, mobile;
            wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobile = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifi.isConnectedOrConnecting() || mobile.isConnectedOrConnecting()) {
                return true;
            }


        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    public static void savePref(Context context,String name,String value){
        SharedPreferences sp=context.getSharedPreferences(Globals.APP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(name,value);
        editor.commit();
    }
    public static String getPref(Context context,String name){
        SharedPreferences sp=context.getSharedPreferences(Globals.APP_NAME,Context.MODE_PRIVATE);
        return sp.getString(name,"na");
    }

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }




    public static void sendNotfication(String title,String msg){

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent=new Intent(MyApp.getAppContext(),MainActivity.class);
        PendingIntent pintent= PendingIntent.getActivity(MyApp.getAppContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification=new NotificationCompat.Builder(MyApp.getAppContext())
                .setContentTitle(title)
                .setContentText(msg)
                .setContentIntent(pintent)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.main_myplaylist)
                .build();

        NotificationManager notificationMana= (NotificationManager) MyApp.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationMana.notify(0, notification);
    }

    public static String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        return time;
    }
}
