package com.crown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import com.crown.time.VirtualClock;

public class VirtualClockTest {
    @Test
    public void testStartStop() {
        VirtualClock vc = new VirtualClock(1, new Runnable() {
                @Override
                public void run() {
                    // Do nothing.
                }
            });

        vc.startAt(Instant.now());

        // XXX: No way to stop the clock yet.
    }
}
