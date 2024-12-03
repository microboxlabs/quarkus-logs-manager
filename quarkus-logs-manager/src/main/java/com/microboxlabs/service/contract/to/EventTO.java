package com.microboxlabs.service.contract.to;

import java.io.Serial;
import java.io.Serializable;

public class EventTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8805695841848437089L;

    private String id;
    private String eventCode;
    private String eventTime;
    private LogTO payload;

    public EventTO() {
    }

    public EventTO(EventTO event) {
        this.id = event.getId();
        this.eventCode = event.getEventCode();
        this.eventTime = event.getEventTime();
        this.payload = event.getPayload();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public LogTO getPayload() {
        return payload;
    }

    public void setPayload(LogTO payload) {
        this.payload = payload;
    }
}
