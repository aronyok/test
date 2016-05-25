package baajna.scroll.owner.mobioapp.asynctask;//package com.mobioapp.gaanpagla.com.mobioapp.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;
import baajna.scroll.owner.mobioapp.interfaces.OnTaskCompleted;

public class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

    private int loginStatus;
    private ProgressDialog dlog;
    private Activity mActivity;
    private OnTaskCompleted onTaskCompleted;
    private String func_id;
    private String email;
    private String pass;

    public LoginAsyncTask(Activity context, String email, String pass, OnTaskCompleted onTaskCompleted) {
        this.mActivity = context;
        this.onTaskCompleted = onTaskCompleted;
//		this.func_id = func_id;
        this.email = email;
        this.pass = pass;
    }

    @Override
    protected void onPreExecute() {
        dlog = ProgressDialog.show(mActivity, "Loading", "Please wait", true, false);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        try {
            loginStatus = CommunicationLayer.getLoginData(email, pass);
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

        Log.i("Log in is ", "---" + loginStatus);

        onTaskCompleted.onTaskCompleted(loginStatus);

    }
}
