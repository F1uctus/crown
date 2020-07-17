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
        timeline.pendingActions.forEach((timePoint, action) -> {
            if (timePoint.isBefore(Timeline.getGameClock().now())) {
                action.perform();
                timeline.pendingActions.remove(action.getPoint());
                timeline.performedActions.add(action);
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
