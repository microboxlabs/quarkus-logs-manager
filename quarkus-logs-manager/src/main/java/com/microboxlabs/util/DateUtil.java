package com.microboxlabs.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.function.Function;

public class DateUtil {
    private static final DateTimeFormatter LOG_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final Function<String, LocalDateTime> parseLogDateTime = timestamp -> LocalDateTime.parse(timestamp, LOG_DATE_FORMAT);
    private static final String AWS_EVENT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final Function<LocalDateTime, String> convertAWSEventTOString = d -> DateTimeFormatter.ofPattern(AWS_EVENT_DATE_PATTERN).format(d);

    public static final DateTimeFormatter instantFormatter = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(Locale.US)
            .withZone(ZoneId.systemDefault());
}
