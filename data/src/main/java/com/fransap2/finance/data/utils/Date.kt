package com.fransap2.finance.data.utils

import java.time.Instant
import java.time.temporal.ChronoUnit

// API timestamps are in UTC-4, so this will adjust them to UTC
internal fun Instant.adjustApiTimeStamp(): Instant {
    return plus(4, ChronoUnit.HOURS)
}