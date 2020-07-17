package com.crown.time;

import com.crown.common.NamedObject;
import com.crown.i18n.ITemplate;

public class TimelineMirrorAction extends NamedObject implements Runnable {
    private final Timeline timeline;

    TimelineMirrorAction(Timeline timeline) {
        super("");
        this.timeline = timeline;
    }

    @Override
    public void run() {
        timeline.pendingActions.forEach((actionTimePoint, action) -> {
            if (actionTimePoint.isBefore(Timeline.getGameClock().now().minus(timeline.getOffsetToMain()))) {
                action.perform();
                timeline.pendingActions.remove(actionTimePoint);
                timeline.performedActions.put(actionTimePoint, action);
            }
        });
    }

    @Override
    public ITemplate getName() {
        return null;
    }

    @Override
    public ITemplate getDescription() {
        return null;
    }
}
