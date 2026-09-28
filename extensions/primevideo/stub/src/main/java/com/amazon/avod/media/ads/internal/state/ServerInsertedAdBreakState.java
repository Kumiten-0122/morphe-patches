package com.amazon.avod.media.ads.internal.state;

import com.amazon.avod.fsm.Trigger;
import com.amazon.avod.media.playback.state.trigger.PlayerTriggerType;

public class ServerInsertedAdBreakState extends AdBreakState {
    private boolean mIsChapteredBreak;

    public String isChaptered2() {
        return this.mIsChapteredBreak ? "TRUE" : "FALSE";
    }

     public void exit(Trigger<PlayerTriggerType> trigger) {
    }

}
