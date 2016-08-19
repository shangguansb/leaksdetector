package com.example.modao.leaksdetector;

import android.app.Application;
import android.content.Context;

import com.example.leaksdetector.android.LeakCanary;
import com.example.leaksdetector.watcher.RefWatcher;

/**
 * Created by modao on 16/8/19.
 */
public class ExampleApplication extends Application {
    public static RefWatcher getRefWatcher(Context context) {
        ExampleApplication application = (ExampleApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }

}
