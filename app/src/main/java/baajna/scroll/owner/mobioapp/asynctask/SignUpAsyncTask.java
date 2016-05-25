package baajna.scroll.owner.mobioapp.asynctask;//package com.mobioapp.gaanpagla.com.mobioapp.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import baajna.scroll.owner.mobioapp.interfaces.OnTaskCompleted;
import baajna.scroll.owner.mobioapp.parser.CommunicationLayer;

public class SignUpAsyncTask extends AsyncTask<Void, Void, Void> {

    private int signupStatus;
    private ProgressDialog dlog;
    private Activity mActivity;
    private OnTaskCompleted onTaskCompleted;
    private String func_id;
    private String email;
    private String pass;

    public SignUpAsyncTask(Activity context, String email, String pass, OnTaskCompleted onTaskCompleted) {
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
            signupStatus = CommunicationLayer.getSignUpData(email, pass);
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

        Log.i("Sing up is ", "---" + signupStatus);

        onTaskCompleted.onTaskCompleted(signupStatus);

    }
}
