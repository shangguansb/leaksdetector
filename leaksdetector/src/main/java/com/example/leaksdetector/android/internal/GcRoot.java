package com.example.leaksdetector.android.internal;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.example.leaksdetector.android.AndroidExcludedRefs;
import com.example.leaksdetector.android.AndroidHeapDumper;
import com.example.leaksdetector.android.DefaultLeakDirectoryProvider;
import com.example.leaksdetector.android.DisplayLeakService;
import com.example.leaksdetector.android.LeakDirectoryProvider;
import com.example.leaksdetector.android.ServiceHeapDumpListener;
import com.example.leaksdetector.watcher.ExcludedRefs;
import com.example.leaksdetector.watcher.HeapDump;
import com.example.leaksdetector.watcher.HeapDumper;
import com.example.leaksdetector.watcher.KeyedWeakReference;

import java.io.File;
import java.lang.ref.ReferenceQueue;
import java.util.UUID;

import static com.example.leaksdetector.watcher.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Created by modao on 16/8/18.
 */
public class GcRoot {
    private HeapDump.Listener heapdumpListener;
    Context context;
    private final ReferenceQueue<Object> queue;
    private final ExcludedRefs excludedRefs;

    public static android.os.Handler handler;

    public GcRoot(Context ctx) {
        this.context = ctx;
        queue = new ReferenceQueue<>();
        this.excludedRefs = checkNotNull(AndroidExcludedRefs.createAppDefaults().build(), "excludedRefs");

    }


    public KeyedWeakReference getKeyedWeakReference(Object watchedReference) {
        return getKeyedWeakReference(watchedReference, "");
    }

    public KeyedWeakReference getKeyedWeakReference(Object watchedReference, String referenceName) {
        checkNotNull(watchedReference, "watchedReference");
        checkNotNull(referenceName, "referenceName");
        String key = UUID.randomUUID().toString();
        final KeyedWeakReference reference =
                new KeyedWeakReference(watchedReference, key, referenceName, queue);
        return reference;
    }

    public void getGcRoot(Object watchedReference, Handler handle) {
        if (context != null) {
            Toast.makeText(context, "LeaksDetector Analyse-ing！", Toast.LENGTH_SHORT).show();
        }
        watch(watchedReference);
        this.handler = handle;
    }

    void watch(Object watchedReference) {
        KeyedWeakReference reference = getKeyedWeakReference(watchedReference);
        final long watchStartNanoTime = System.nanoTime();
        long gcStartNanoTime = System.nanoTime();
        long watchDurationMs = NANOSECONDS.toMillis(gcStartNanoTime - watchStartNanoTime);
        LeakDirectoryProvider leakDirectoryProvider = new DefaultLeakDirectoryProvider(context);
        AndroidHeapDumper heapDumper = new AndroidHeapDumper(context, leakDirectoryProvider);
        heapdumpListener = new ServiceHeapDumpListener(context.getApplicationContext(), DisplayLeakService.class);
        File heapDumpFile = heapDumper.dumpHeap();

        if (heapDumpFile == HeapDumper.NO_DUMP) {
            // Could not dump the heap, abort.
            if (context != null) {
//                Toast.makeText(context, "modao-无法获取heap信息！", Toast.LENGTH_SHORT).show();
            }
            return;

        }
        long startDumpHeap = System.nanoTime();
        long heapDumpDurationMs = NANOSECONDS.toMillis(System.nanoTime() - startDumpHeap);
        long gcDurationMs = NANOSECONDS.toMillis(startDumpHeap - gcStartNanoTime);
        heapdumpListener.analyze(
                new HeapDump(heapDumpFile, reference.key, reference.name, excludedRefs, watchDurationMs,
                        gcDurationMs, heapDumpDurationMs));

    }
}
