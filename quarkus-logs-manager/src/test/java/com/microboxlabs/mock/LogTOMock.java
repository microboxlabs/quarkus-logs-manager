package com.microboxlabs.mock;

import com.microboxlabs.service.contract.to.LogTO;
import com.microboxlabs.util.DateUtil;

import java.time.LocalDateTime;
import java.util.List;

public class LogTOMock {
    public static LogTO createMock(Long id, String logLevel, String serviceName, String message, LocalDateTime timestamp) {
        LogTO logTO = new LogTO();
        logTO.setId(id);
        logTO.setLogLevel(logLevel);
        logTO.setServiceName(serviceName);
        logTO.setMessage(message);
        logTO.setTimestamp(DateUtil.convertAWSEventTOString.apply(timestamp));
        return logTO;
    }

    public static List<LogTO> createMockList() {
        return List.of(
                createMock(1L, "INFO", "Service-A", "This is a test log message.", LocalDateTime.now()),
                createMock(2L, "ERROR", "Service-B", "This is an error log message.", LocalDateTime.now().minusDays(1))
        );
    }
}
