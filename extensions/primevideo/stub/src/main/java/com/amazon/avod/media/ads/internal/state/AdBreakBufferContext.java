package com.amazon.avod.media.ads.internal.state;

import com.amazon.avod.threading.Tickers;
import com.google.common.base.Stopwatch;
import javax.annotation.concurrent.NotThreadSafe;

/* JADX INFO: loaded from: classes8.dex */
@NotThreadSafe
public class AdBreakBufferContext {
    private int mCurrentBreakBuffers = 0;
    private Stopwatch mBufferStopwatch = new Stopwatch(Tickers.androidTicker());

    private void clearCurrentBreakBuffers() {
        this.mCurrentBreakBuffers = 0;
    }

    private void resetBufferStopwatch() {
        this.mBufferStopwatch.reset();
    }

    public void reset() {
        clearCurrentBreakBuffers();
        resetBufferStopwatch();
    }
}
