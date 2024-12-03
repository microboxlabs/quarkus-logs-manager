package com.microboxlabs.service.contract.to;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.microboxlabs.service.datasource.domain.Log}
 */
public class LogTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4762121180300891133L;

    private Long id;
    private String timestamp;
    private String logLevel;
    private String serviceName;
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "timestamp = " + timestamp + ", " +
                "logLevel = " + logLevel + ", " +
                "serviceName = " + serviceName + ", " +
                "message = " + message + ")";
    }
}