package baajna.scroll.owner.mobioapp.asynctask;//package com.mobioapp.gaanpagla.com.mobioapp.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import baajna.scroll.owner.mobioapp.interfaces.OnTaskCompleted;

public class ArtistListAsyncTask extends AsyncTask<Void, Void, Void> {

    private int artistStatus;
    private ProgressDialog dlog;
    private Activity mActivity;
    private OnTaskCompleted onTaskCompleted;

    public ArtistListAsyncTask(Activity context, OnTaskCompleted onTaskCompleted) {
        this.mActivity = context;
        this.onTaskCompleted = onTaskCompleted;
    }

    @Override
    protected void onPreExecute() {
        dlog = ProgressDialog.show(mActivity, "Loading", "Please wait", true, false);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        /*try {
            artistStatus = CommunicationLayer.parseArtistListData();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (dlog.isShowing()) {
            dlog.dismiss();
        }

        Log.i("Artist_List_is ", "---" + artistStatus);

        onTaskCompleted.onTaskCompleted(artistStatus);

    }
}
