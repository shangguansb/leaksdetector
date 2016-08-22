package com.example.modao.leaksdetector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
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

import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    static TextView tx;
    List list = new ArrayList();
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
        final Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.avator);
        RefWatcher refWatcher = LeakCanary.androidWatcher(getApplicationContext(),
                new ServiceHeapDumpListener(getApplicationContext(), DisplayLeakService.class),
                AndroidExcludedRefs.createAppDefaults().build());
        //将显示结果的Activity设置成可用
        LeakCanary.enableDisplayLeakActivity(getApplicationContext());
//        tx.setBackground(new BitmapDrawable(bm));
        Object ob = new Object();
        list.add(ob);
        final Object ondw = new Object();
        final GcRoot mygc = new GcRoot(getApplicationContext());
//        refWatcher.watch(bm);
//        refWatcher.watch(ob);
        mButtonShowGC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mygc.getGcRoot(bm, handle);
            }
        });
    }

    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mTextGC.setText((String) (msg.obj));
        }
    };
}
