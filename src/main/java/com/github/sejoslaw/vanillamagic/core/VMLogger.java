package com.github.sejoslaw.vanillamagic.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMLogger {
    public static Logger LOGGER = LogManager.getLogger("VanillaMagic");

    public static void log(Level level, String message) {
        LOGGER.log(level, message);
    }

    public static void logInfo(String message) {
        log(Level.INFO, message);
    }
}
