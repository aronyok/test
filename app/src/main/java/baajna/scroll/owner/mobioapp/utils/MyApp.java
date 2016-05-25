package baajna.scroll.owner.mobioapp.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jewel on 1/17/2016.
 */
public class MyApp extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
