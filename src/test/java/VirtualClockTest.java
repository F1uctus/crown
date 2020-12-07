import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import com.crown.time.VirtualClock;

public class VirtualClockTest {
    @Test
    public void testStartStop() {
        VirtualClock vc = new VirtualClock(1, () -> {
            // Do nothing.
        });

        vc.startAt(Instant.now());
        vc.paused = true;

        assertEquals(Duration.between(vc.initial(), vc.now()), Duration.ZERO);
    }

    @Test
    public void testDuration() throws InterruptedException {
        VirtualClock vc = new VirtualClock(1, () -> {
            // Do nothing.
        });

        vc.startAt(Instant.now());
        Thread.sleep(1000);
        vc.paused = true;

        assertEquals(
            Duration.between(vc.initial(), vc.now()).toMillis(),
            1000,
            50
        );
    }
}
