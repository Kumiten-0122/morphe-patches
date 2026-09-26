package com.amazon.avod.playback.session;

import com.amazon.avod.media.playback.VideoPlayerBase;
import com.amazon.avod.playback.PlaybackExperienceController;

public class AmazonVideoPlayer extends VideoPlayerBase implements PlaybackExperienceController {

    public long getCurrentPosition() {
        long totalMilliseconds = 0;
        return totalMilliseconds;
    }

    public void seekTo(long positionMs) {
    }

    public void pause() {
    }

    public void start() {
    }

    public boolean isPlaying(){
    }
    
}
