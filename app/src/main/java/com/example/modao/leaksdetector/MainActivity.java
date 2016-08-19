package com.example.modao.leaksdetector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.StaticLayout;
import android.widget.TextView;

import com.example.leaksdetector.android.LeakCanary;
import com.example.leaksdetector.watcher.RefWatcher;

public class MainActivity extends AppCompatActivity {
    static TextView tx;
//    static Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LeakCanary.install(getApplication());
        setContentView(R.layout.activity_main);
        tx = new TextView(MainActivity.this);
//        bm = BitmapFactory.decodeResource(getResources(), R.drawable.avator);
//        RefWatcher refWatcher = ExampleApplication.getRefWatcher(getApplication());
//        refWatcher.watch(bm);
    }
}
