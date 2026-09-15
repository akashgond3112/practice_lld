package com.java.lld.design.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record CookieLogEntry(String cookie, OffsetDateTime timestamp) {

    public boolean occurredOn(LocalDate date) {
        return timestamp.atZoneSameInstant(ZoneOffset.UTC).toLocalDate().equals(date);
    }

    public boolean occurredAfter(LocalDate date) {
        return timestamp.atZoneSameInstant(ZoneOffset.UTC).toLocalDate().isAfter(date);
    }
}
