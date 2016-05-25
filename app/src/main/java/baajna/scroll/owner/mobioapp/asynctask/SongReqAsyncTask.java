package baajna.scroll.owner.mobioapp.asynctask;//package com.mobioapp.gaanpagla.com.mobioapp.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import baajna.scroll.owner.mobioapp.interfaces.OnTaskCompleted;
import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;

public class SongReqAsyncTask extends AsyncTask<Void, Void, Void> {

    private int songReqStatus;
    private ProgressDialog dlog;
    private Activity mActivity;
    private OnTaskCompleted onTaskCompleted;
    String id;

    public SongReqAsyncTask(Activity context, String song_id, OnTaskCompleted onTaskCompleted) {
        this.mActivity = context;
        this.onTaskCompleted = onTaskCompleted;
        this.id = song_id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dlog = ProgressDialog.show(mActivity, "Loading", "Please wait", true, false);
        dlog.setCancelable(false);

//
//		dialog = new ProgressDialog(MainActivity.this);
//		dialog.setMessage("Loading, please wait");
//		dialog.setTitle("Connecting server");
//		dialog.show();
//		dialog.setCancelable(false);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        try {
            songReqStatus = CommunicationLayer.getSongRequestData(id);
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

        Log.i("Album_List_is ", "---" + songReqStatus);

        onTaskCompleted.onTaskCompleted(songReqStatus);

    }
}
