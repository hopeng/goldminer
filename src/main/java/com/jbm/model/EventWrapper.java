package com.jbm.model;

import com.jbm.hazelcast.sample.JsonUtils;

/**
 * Created by hopeng on 19/09/2016.
 */
public class EventWrapper {

    private EventType eventType;

    private String payload;

    public EventWrapper() {
    }

    public EventWrapper(EventType eventType, Object payload) {
        this.eventType = eventType;
        this.payload = String.valueOf(payload);
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
