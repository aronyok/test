package baajna.scroll.owner.mobioapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtil {

    Context mContext;
    public SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;

    private final String CURRENT_CREDITS = "current_credits";
    private final String CURRENT_PAID = "current_paid";

    public PreferenceUtil(Context mContext) {
        super();
        this.mContext = mContext;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public int getCurrentCredits() {

        return sharedPreferences.getInt(CURRENT_CREDITS, 200);
    }

    public void setCurrentCredits(int credits) {

        spEditor = sharedPreferences.edit();
        spEditor.putInt(CURRENT_CREDITS, credits);
        spEditor.commit();
    }

    public int getPaidPoint() {

        return sharedPreferences.getInt(CURRENT_PAID, 0);
    }

    public void setPaidPoint(int credits) {

        spEditor = sharedPreferences.edit();
        spEditor.putInt(CURRENT_PAID, credits);
        spEditor.commit();
    }
}