package com.java.lld.design;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChangeLog {
    private final List<ChangeEvent> events = new ArrayList<>();

    public void append(ChangeEvent event) {
        events.add(event);
    }

    public int size() {
        return events.size();
    }

    public ChangeEvent get(int index) {
        return events.get(index);
    }

    public List<ChangeEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }
}
