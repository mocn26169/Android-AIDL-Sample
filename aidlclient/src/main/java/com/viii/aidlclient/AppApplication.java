package com.viii.aidlclient;

import android.app.Application;
import android.util.Log;

/**
 * Created by m on 2017/3/4.
 */

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(getClass().getSimpleName(),"AppApplication onCreate");
    }
}
