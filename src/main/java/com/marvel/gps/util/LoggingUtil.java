package com.marvel.gps.util;

import com.marvel.gps.constants.LoggingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;
import static net.logstash.logback.marker.Markers.append;

public class LoggingUtil {
    private static final String SERVICE_NAME = "GlobalProvisioningService";
    private final Logger logger;

    public LoggingUtil(Class<?> clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    public LoggingUtil(String clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    public void info(String message, String correlationId) {
        logger.info(message,
                append(LoggingConstants.CORELATION_ID, correlationId),
                append(LoggingConstants.APPLICATION, SERVICE_NAME),
                append(LoggingConstants.TIMESTAMP, getCurrentTime()));
    }

    public void warn(String message, String correlationId) {
        logger.warn(message,
                append(LoggingConstants.CORELATION_ID, correlationId),
                append(LoggingConstants.APPLICATION, SERVICE_NAME),
                append(LoggingConstants.TIMESTAMP, getCurrentTime()));
    }

    public void warn(String message) {
        warn(message, "", "", "");
    }

    public void warn(String format, Object... args) {
        warn(format(format, args));
    }

    public void error(String message, String correlationId) {
        logger.error(message,
                append(LoggingConstants.MESSAGE, message),
                append(LoggingConstants.CORELATION_ID, correlationId),
                append(LoggingConstants.APPLICATION, SERVICE_NAME),
                append(LoggingConstants.TIMESTAMP, getCurrentTime()));
    }

    public void error(String message) {
        error(message, "", "", "");
    }

    public void error(String format, Object... args) {
        error(format(format, args));
    }

    public void debug(String message, String correlationId) {
        logger.debug(message,
                append(LoggingConstants.CORELATION_ID, correlationId),
                append(LoggingConstants.APPLICATION, SERVICE_NAME),
                append(LoggingConstants.TIMESTAMP, getCurrentTime()));
    }

    public void debug(String message) {
        debug(message, "");
    }

    public void debug(String format, Object... args) {
        debug(format(format, args));
    }

    private String getCurrentTime() {
        final Instant now = Instant.now();
        return DateTimeFormatter.ISO_INSTANT.format(now);
    }

}
