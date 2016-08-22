package com.example.modao.leaksdetector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

import com.example.leaksdetector.android.AndroidExcludedRefs;
import com.example.leaksdetector.android.DisplayLeakService;
import com.example.leaksdetector.android.LeakCanary;
import com.example.leaksdetector.android.ServiceHeapDumpListener;
import com.example.leaksdetector.android.internal.GcRoot;
import com.example.leaksdetector.watcher.RefWatcher;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static TextView tx;
    static List list = new ArrayList();
    Button mButtonShowGC;
    TextView mTextGC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LeakCanary.install(getApplication());
        setContentView(R.layout.activity_main);
        mButtonShowGC = (Button) findViewById(R.id.show_gc);
        mTextGC = (TextView) findViewById(R.id.text_gc);
        tx = new TextView(MainActivity.this);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.avator);
        RefWatcher refWatcher = LeakCanary.androidWatcher(getApplicationContext(),
                new ServiceHeapDumpListener(getApplicationContext(), DisplayLeakService.class),
                AndroidExcludedRefs.createAppDefaults().build());
        //将显示结果的Activity设置成可用
        LeakCanary.enableDisplayLeakActivity(getApplicationContext());
        tx.setBackground(new BitmapDrawable(bm));
        Object ob = new Object();
        list.add(ob);
        GcRoot mygc = new GcRoot(getApplicationContext());
        mygc.getgc(bm);
//        refWatcher.watch(bm);
        refWatcher.watch(ob);

    }
}
