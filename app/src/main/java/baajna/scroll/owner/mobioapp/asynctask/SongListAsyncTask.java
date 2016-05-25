package baajna.scroll.owner.mobioapp.asynctask;

/**
 * Created by anuradha on 8/11/15.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import baajna.scroll.owner.mobioapp.interfaces.OnTaskCompleted;
import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;

public class SongListAsyncTask extends AsyncTask<Void, Void, Void> {

    private int songStatus;
    private ProgressDialog dlog;
    private Activity mActivity;
    private OnTaskCompleted onTaskCompleted;
    int albumId;
    private boolean isArtist;

    public SongListAsyncTask(Activity context, int id, boolean isArtist, OnTaskCompleted onTaskCompleted) {
        this.mActivity = context;
        this.onTaskCompleted = onTaskCompleted;
        albumId = id;
        this.isArtist = isArtist;
    }


    @Override
    protected void onPreExecute() {
        dlog = ProgressDialog.show(mActivity, "Loading", "Please wait", true, false);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        try {
            songStatus = CommunicationLayer.getSongListData(isArtist, albumId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (dlog.isShowing()) {
            dlog.dismiss();
        }

        Log.i("Song_List_is", "---" + songStatus);

        onTaskCompleted.onTaskCompleted(songStatus);

    }
}
