package com.amazon.avod.media.ads.internal.state;

public class AdBreakBufferContext {
    private int mCurrentBreakBuffers = 0;

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
