package seia.vanillamagic.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.logging.log4j.Level;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import seia.vanillamagic.api.quest.IQuest;
import seia.vanillamagic.core.VanillaMagic;

/**
 * Class which operates on additional VM configuration files.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMConfigAchievements {
	public static final String VM_DIRECTORY = "/VanillaMagic/";

	private final String achievements = "Quests.json";
	private final String aboutAchievements = "About Quests.txt";

	private File modConfigurationDirectory;
	private File modFile;
	private File fileAchievements;
	private File fileAboutAchievements;

	/**
	 * modConfigurationDirectory - config/VanillaMagic/ <br>
	 * modFile - vanillamagic-[].jar OR /bin/ (if modding)
	 */
	public VMConfigAchievements(File modConfigurationDirectory, File modFile) {
		this.modConfigurationDirectory = modConfigurationDirectory;
		this.modFile = modFile;

		if (!this.modConfigurationDirectory.exists()) {
			this.modConfigurationDirectory.mkdirs();
			VanillaMagic.logInfo("VanillaMagic config directory created.");
		}

		VanillaMagic.logInfo("VanillaMagic config directory loaded.");
		this.fileAchievements = unzip(this.achievements, fileAchievements);
		this.fileAboutAchievements = unzip(this.aboutAchievements, fileAboutAchievements);
		readAchievements();
	}

	/**
	 * Unzip the specified file from JAR - copy into directory from inside of JAR
	 * file.
	 */
	@SuppressWarnings("resource")
	public File unzip(String fileName, File localFile) {
		localFile = new File(this.modConfigurationDirectory, fileName);

		if (!localFile.exists()) {
			try {
				localFile.createNewFile();

				if (!this.modFile.isDirectory()) {
					JarFile jarFile = new JarFile(this.modFile);
					Enumeration<JarEntry> enumeration = jarFile.entries();

					while (enumeration.hasMoreElements()) {
						JarEntry file = (JarEntry) enumeration.nextElement();

						if (file.getName().equals(fileName)) {
							InputStream is = jarFile.getInputStream(file);
							FileOutputStream fos = new FileOutputStream(localFile);

							while (is.available() > 0) {
								fos.write(is.read());
							}

							fos.close();
							is.close();
							VanillaMagic.logInfo(fileName + " created.");
							return localFile;
						}
					}
				} else if (this.modFile.isDirectory()) {
					File[] files = this.modFile.listFiles();

					for (File file : files) {
						if (file.getName().contains(fileName)) {
							FileInputStream is = new FileInputStream(file);
							FileOutputStream fos = new FileOutputStream(localFile);

							while (is.available() > 0) {
								fos.write(is.read());
							}

							fos.close();
							is.close();
							VanillaMagic.logInfo(fileName + " created.");
							return localFile;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				VanillaMagic.logInfo("Error while creating " + fileName);
			}
		}
		return localFile;
	}

	/**
	 * Read all advancements from advancements.json file
	 */
	public void readAchievements() {
		try {
			FileInputStream fis = new FileInputStream(this.fileAchievements);
			InputStreamReader reader = new InputStreamReader(fis);
			JsonParser parser = new JsonParser();
			JsonElement js = parser.parse(reader);

			if (js.isJsonArray()) {
				JsonArray ja = js.getAsJsonArray();

				for (JsonElement je : ja) {
					JsonObject jo = je.getAsJsonObject();
					Class<?> className = Class.forName(jo.get("questClass").getAsString()); // Read Quest class.
					IQuest quest = (IQuest) className.newInstance(); // Create Quest instance
					quest.readData(jo); // Read Quest data.
				}

				VanillaMagic.log(Level.WARN, "VanillaMagic Quests readded from JSON file.");
			} else {
				VanillaMagic.log(Level.WARN, "Can't load VanillaMagic Quests from JSON file.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			VanillaMagic.logInfo("Error while loading Quests.json");
		}
	}
}