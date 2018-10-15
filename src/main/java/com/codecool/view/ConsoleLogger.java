package com.codecool.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleLogger {
    private static Logger LOG = LoggerFactory
            .getLogger(ConsoleLogger.class);

    public static void info(String message) {
        LOG.info(message);
    }

    public static void warn(String message) {
        LOG.warn(message);
    }

    public static void error(String message) {
        LOG.error(message);
    }

    public static void debug(String message) {
        LOG.debug(message);
    }
}
