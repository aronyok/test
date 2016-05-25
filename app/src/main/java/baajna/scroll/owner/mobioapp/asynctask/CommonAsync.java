package baajna.scroll.owner.mobioapp.asynctask;//package com.mobioapp.gaanpagla.com.mobioapp.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;
import baajna.scroll.owner.mobioapp.interfaces.OnTaskCompleted;

public class CommonAsync extends AsyncTask<Void, Void, Void> {

    private int albumStatus;
    private ProgressDialog dlog;
    private Context mActivity;
    private OnTaskCompleted onTaskCompleted;

    public CommonAsync(Context context) {
        this.mActivity = context;
        this.onTaskCompleted = (OnTaskCompleted) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //dlog = ProgressDialog.show(mActivity, "Loading", "Please wait", true, false);
        //dlog.setCancelable(false);

    }

    @Override
    protected Void doInBackground(Void... arg0) {

        try {
            albumStatus = CommunicationLayer.getAlbumListData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
       /* if (dlog.isShowing()) {
            dlog.dismiss();
        }

        Log.i("Album_List_is ", "---" + albumStatus);*/

        onTaskCompleted.onTaskCompleted(albumStatus);

    }
}
