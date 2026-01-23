package com.jasonvosmn.parasiteshud.util;

import org.apache.logging.log4j.LogManager;

public class Logger {
    // Используем полное имя класса, чтобы избежать конфликта
    private static final org.apache.logging.log4j.Logger LOGGER =
            LogManager.getLogger("SrpHUD");

    public static void info(Object msg) {
        LOGGER.info(msg);
    }

    public static void warn(Object msg) {
        LOGGER.warn(msg);
    }

    public static void error(Object msg) {
        LOGGER.error(msg);
    }

    public static void debug(Object msg) {
        LOGGER.debug(msg);
    }

    public static boolean isDebugEnabled() {
        return LOGGER.isDebugEnabled();
    }
}
