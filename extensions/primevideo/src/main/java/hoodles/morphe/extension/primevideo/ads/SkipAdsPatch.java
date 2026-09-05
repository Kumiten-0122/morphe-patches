```java
package hoodles.morphe.extension.primevideo.ads;

import android.os.Handler;
import android.os.Looper;

import com.amazon.avod.fsm.SimpleTrigger;
import com.amazon.avod.media.ads.AdBreak;
import com.amazon.avod.media.ads.internal.state.AdBreakTrigger;
import com.amazon.avod.media.ads.internal.state.AdEnabledPlayerTriggerType;
import com.amazon.avod.media.ads.internal.state.ServerInsertedAdBreakState;
import com.amazon.avod.media.playback.VideoPlayer;

import app.morphe.extension.shared.Logger;

@SuppressWarnings("unused")
public final class SkipAdsPatch {
    private static final Handler HANDLER =
            new Handler(Looper.getMainLooper());

    public static void enterServerInsertedAdBreakState(
            ServerInsertedAdBreakState state,
            AdBreakTrigger trigger,
            VideoPlayer player) {

        try {
            AdBreak adBreak = trigger.getBreak();

            // Determine seek target.
            final long seekTarget;

            if (trigger.getSeekStartPosition() != null) {
                seekTarget = trigger.getSeekTarget().getTotalMilliseconds();
            } else {
                seekTarget = player.getCurrentPosition()
                        + adBreak.getDurationExcludingAux().getTotalMilliseconds();
            }

            Logger.printDebug(() ->
                    "[SkipAds] burst seek target=" + seekTarget);

            // 全広告に対して疑似シークを実行。
            // 疑似シーク完了後、最後に広告終了位置へ強制ジャンプする。
            burstSeek(player, seekTarget);

            // Send "end of ads" trigger to state machine.
            state.doTrigger(new SimpleTrigger(
                    AdEnabledPlayerTriggerType.NO_MORE_ADS_SKIP_TRANSITION));

        } catch (Exception ex) {
            Logger.printException(() ->
                    "Failed skipping ads", ex);
        }
    }

    private static void burstSeek(VideoPlayer player, long target) {
        // 疑似シークは2回
        long[] offsets = new long[] {
                -1500L,
                0L
        };

        long delay = 0L;

        for (long offset : offsets) {
            final long seekPos =
                    Math.max(0L, target + offset);

            HANDLER.postDelayed(() -> {
                try {
                    player.seekTo(seekPos);

                    Logger.printDebug(() ->
                            "[SkipAds] burst seekTo=" + seekPos);

                } catch (Throwable ignored) {
                }
            }, delay);

            delay += 40L;
        }

        // 疑似シーク完了後、広告終了位置へ強制ジャンプ。
        final long finalDelay = delay;

        HANDLER.postDelayed(() -> {
            try {
                player.seekTo(target);

                Logger.printDebug(() ->
                        "[SkipAds] final seekTo=" + target);

            } catch (Throwable ignored) {
            }
        }, finalDelay);
    }
}
```
