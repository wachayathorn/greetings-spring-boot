package com.wachayathorn.greetings_spring_boot.support;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class MutableThailandClock extends Clock {

    private volatile Instant instant;
    private final ZoneId zone = ZoneId.of("Asia/Bangkok");

    public MutableThailandClock(Instant instant) {
        this.instant = instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Instant instant() {
        return instant;
    }
}
