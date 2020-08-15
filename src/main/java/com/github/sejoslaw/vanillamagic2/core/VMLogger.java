package com.github.sejoslaw.vanillamagic2.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class VMLogger {
    public static Logger LOGGER = LogManager.getLogger("VanillaMagic2");

    public static void log(Level level, String message) {
        LOGGER.log(level, message);
    }

    public static void logInfo(String message) {
        log(Level.INFO, message);
    }

    public static void logError(String message) {
        log(Level.ERROR, message);
    }
}
