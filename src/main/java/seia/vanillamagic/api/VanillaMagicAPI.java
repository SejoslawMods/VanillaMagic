package seia.vanillamagic.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is API for Minecraft Mod -> Vanilla Magic <br>
 * Link to project: https://github.com/Sejoslaw/VanillaMagic
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VanillaMagicAPI {
	/**
	 * Year.Month of the API release.
	 */
	public static final String VERSION = "2019.01";
	public static final Logger LOGGER = LogManager.getLogger("VanillaMagicAPI");

	private VanillaMagicAPI() {
	}

	public static void log(Level level, String log) {
		LOGGER.log(level, log);
	}

	public static void logInfo(String message) {
		log(Level.INFO, message);
	}
}