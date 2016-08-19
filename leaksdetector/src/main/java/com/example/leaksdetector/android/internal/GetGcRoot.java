package com.example.leaksdetector.android.internal;

import android.content.Context;

import com.example.leaksdetector.leakcanary.AnalysisResult;
import com.example.leaksdetector.watcher.HeapDump;

import java.io.File;
import java.util.List;

import static com.example.leaksdetector.android.LeakCanary.leakInfo;

/**
 * Created by modao on 16/8/18.
 */
public class GetGcRoot {
    List<Leak> leaks;    //寻找初始化的地方
    String visibleLeakRefKey;
    static class Leak {
        final HeapDump heapDump;
        final AnalysisResult result;
        final File resultFile;

        Leak(HeapDump heapDump, AnalysisResult result, File resultFile) {
            this.heapDump = heapDump;
            this.result = result;
            this.resultFile = resultFile;
        }
    }

    public String getStringLeakInfo(Context ctx) {
        Leak visibleLeak = getVisibleLeak();
        String leakInfo = leakInfo(ctx, visibleLeak.heapDump, visibleLeak.result, true);
        return leakInfo;
    }


    Leak getVisibleLeak() {
        if (leaks == null) {
            return null;
        }
        for (Leak leak : leaks) {
            if (leak.heapDump.referenceKey.equals(visibleLeakRefKey)) {
                return leak;
            }
        }
        return null;
    }
}
