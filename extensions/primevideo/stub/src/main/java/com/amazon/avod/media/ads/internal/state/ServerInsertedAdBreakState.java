package com.amazon.avod.media.ads.internal.state;

import com.amazon.avod.fsm.Trigger;
import com.amazon.avod.media.playback.state.trigger.PlayerTriggerType;

public class ServerInsertedAdBreakState extends AdBreakState {
     @Override // com.amazon.avod.media.ads.internal.state.AdEnabledPlaybackState, com.amazon.avod.fsm.StateBase, com.amazon.avod.fsm.State
     public void exit(Trigger<PlayerTriggerType> trigger) {

    }

}
