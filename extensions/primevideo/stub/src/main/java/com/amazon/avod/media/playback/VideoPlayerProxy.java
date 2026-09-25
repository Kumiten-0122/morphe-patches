package com.amazon.avod.media.playback;

public abstract class VideoPlayerProxy implements VideoPlayer {

    public abstract VideoPlayer getDelegate();

    @Override // com.amazon.avod.media.playback.VideoPlayer
    public void seekTo(long positionMs) {
        getDelegate().seekTo(positionMs);
    }

}
