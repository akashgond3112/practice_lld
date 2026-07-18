package com.java.lld.design;

import java.util.ArrayList;
import java.util.List;

public class ChangeCaptureService {
    private final ChangeLog changeLog = new ChangeLog();
    private final List<ChangeListener> listeners = new ArrayList<>();

    public void subscribe(ChangeListener listener) {
        listeners.add(listener);
    }

    public void publish(ChangeEvent event) {
        changeLog.append(event);
        for (ChangeListener listener : listeners) {
            listener.onChange(event);
        }
    }

    public ChangeLog getChangeLog() {
        return changeLog;
    }
}
