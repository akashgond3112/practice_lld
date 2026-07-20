package com.java.lld.design;

import java.util.ArrayList;
import java.util.List;

public class DemoConsumer implements ChangeListener {
    private final List<ChangeEvent> receivedEvents = new ArrayList<>();

    @Override
    public void onChange(ChangeEvent event) {
        receivedEvents.add(event);
        System.out.println("Consumer received: " + event);
    }

    public List<ChangeEvent> getReceivedEvents() {
        return receivedEvents;
    }
}
