package baajna.scroll.owner.mobioapp.asynctask;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.mobioapp.baajna.R;

import baajna.scroll.owner.mobioapp.utils.MyApp;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.utils.Globals;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Jewel on 12/21/2015.
 */
public class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {

    String contentId = "0";
    String fileLocation = null, fileName = "";
    private Builder builder;
    private NotificationManager notifyMan;


    public DownloadAsyncTask(String contentId, String fileName) {
        this.contentId = contentId;
        this.fileName = fileName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //PendingIntent pIntent=PendingIntent.getActivity(MyApp.getAppContext(),1,new Intent(MyApp.getAppContext(), MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
        notifyMan = (NotificationManager) MyApp.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(MyApp.getAppContext());
        builder.setContentTitle("Download started").setContentText("Download in progress");
        builder.setSmallIcon(R.drawable.sync_icon);
        builder.setProgress(100, 0, false);
        //builder.setDeleteIntent(pIntent);
        notifyMan.notify(2, builder.build());

    }

    @Override
    protected Boolean doInBackground(String... params) {

        int count;
        Log.d("Jewel", params[0]);
        try {
            URL url = new URL(params[0]);

            URLConnection conn = url.openConnection();
            conn.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = conn.getContentLength();

            // downlod the file
            InputStream input = new BufferedInputStream(url.openStream());
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
            File fDir = new File(dir + Globals.DOWNLOAD_FOLDER);
            if (!fDir.isDirectory()) {
                fDir.mkdirs();
            }
            //Log.d("Jewel","dir: "+dir);
            fileLocation = fDir + "/" + fileName;


            OutputStream output = new FileOutputStream(fileLocation);

            byte data[] = new byte[1024];

            long total = 0;
            //output.write(data1.getBytes());

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                publishProgress((int) (total * 100 / lenghtOfFile));
                output.write(data, 0, count);

            }

            output.flush();
            output.close();
            input.close();
            return true;
        } catch (Exception e) {
            Log.d("Jewel", e.toString());
        }
        return false;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d("Jewel", values[0] + " d");
        builder.setProgress(100, values[0], false);
        builder.setContentText("Download in progress (" + values[0] + "%)");
        notifyMan.notify(2, builder.build());
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {
            builder.setContentTitle("Download completed");
            builder.setContentText("");
            builder.setProgress(100, 0, false);

            notifyMan.notify(2, builder.build());

            DbManager db = new DbManager(MyApp.getAppContext());
            db.addDownloadSong(Integer.valueOf(contentId), fileLocation);
            db.close();
        }


    }
}