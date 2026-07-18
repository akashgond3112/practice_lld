package com.java.lld.design;

import java.time.Instant;

public class ChangeEvent {
    private final String entityId;
    private final ChangeType changeType;
    private final Customer beforeState;
    private final Customer afterState;
    private final Instant timestamp;

    public ChangeEvent(String entityId, ChangeType changeType, Customer beforeState, Customer afterState) {
        this.entityId = entityId;
        this.changeType = changeType;
        this.beforeState = beforeState;
        this.afterState = afterState;
        this.timestamp = Instant.now();
    }

    public String getEntityId() {
        return entityId;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public Customer getBeforeState() {
        return beforeState;
    }

    public Customer getAfterState() {
        return afterState;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "ChangeEvent{" +
                "entityId='" + entityId + '\'' +
                ", changeType=" + changeType +
                ", beforeState=" + beforeState +
                ", afterState=" + afterState +
                ", timestamp=" + timestamp +
                '}';
    }
}
