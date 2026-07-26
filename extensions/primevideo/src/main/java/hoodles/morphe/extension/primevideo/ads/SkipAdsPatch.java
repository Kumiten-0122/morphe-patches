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

    // 動画開始から10秒以内のみ burst seek を使用
    private static final long BURST_THRESHOLD_MS = 10_000L;

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
                    "[SkipAds] current=" + player.getCurrentPosition()
                    + " target=" + seekTarget);

            // 動画開始直後のみ burst seek
            if (player.getCurrentPosition() < BURST_THRESHOLD_MS) {
                Logger.printDebug(() ->
                        "[SkipAds] burst seek");
                burstSeek(player, seekTarget);
            } else {
                Logger.printDebug(() ->
                        "[SkipAds] normal seek");
                player.seekTo(seekTarget);
            }

            // Notify the state machine that ads have ended.
            state.doTrigger(new SimpleTrigger(
                    AdEnabledPlayerTriggerType.NO_MORE_ADS_SKIP_TRANSITION));

        } catch (Exception ex) {
            Logger.printException(() -> "Failed skipping ads", ex);
        }
    }

    private static void burstSeek(VideoPlayer player, long target) {
        long[] offsets = new long[] {
                -1500L,
                -750L,
                0L,
                750L,
                1500L,
                0L
        };

        long delay = 0L;

        for (long offset : offsets) {
            final long seekPos = Math.max(0L, target + offset);

            HANDLER.postDelayed(() -> {
                try {
                    player.seekTo(seekPos);

                    Logger.printDebug(() ->
                            "[SkipAds] seekTo=" + seekPos);

                } catch (Throwable ignored) {
                }
            }, delay);

            delay += 40L;
        }
    }
}