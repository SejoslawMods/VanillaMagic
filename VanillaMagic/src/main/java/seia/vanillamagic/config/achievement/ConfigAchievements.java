package seia.vanillamagic.config.achievement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.logging.log4j.Level;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jline.internal.InputStreamReader;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.quest.QuestList;

public class ConfigAchievements 
{
	public static final String VM_DIRECTORY = "/VanillaMagic/";
	
	public File modConfigurationDirectory;
	public File modFile;
	
	private final String achievements = "achievements.json";
	
	private File fileAchievements;
	
	/**
	 * modConfigurationDirectory - config/VanillaMagic/ <br>
	 * modFile - vanillamagic-[].jar OR /bin/ (if modding)
	 */
	public ConfigAchievements(File modConfigurationDirectory, File modFile) 
	{
		this.modConfigurationDirectory = modConfigurationDirectory;
		this.modFile = modFile;
		if(!this.modConfigurationDirectory.exists())
		{
			this.modConfigurationDirectory.mkdirs();
			VanillaMagic.logger.log(Level.INFO, "VanillaMagic config directory created");
		}
		VanillaMagic.logger.log(Level.INFO, "VanillaMagic config directory loaded");
		unzipAchievementsJSON();
		readAchievements();
	}

	@SuppressWarnings("resource")
	public void unzipAchievementsJSON() 
	{
		this.fileAchievements = new File(modConfigurationDirectory, achievements);
		if(!this.fileAchievements.exists())
		{
			try 
			{
				this.fileAchievements.createNewFile();
				if(!this.modFile.isDirectory())
				{
					JarFile jarFile = new JarFile(this.modFile);
					Enumeration enumeration = jarFile.entries();
					while(enumeration.hasMoreElements())
					{
						JarEntry file = (JarEntry) enumeration.nextElement();
						if(file.getName().equals(this.achievements))
						{
							InputStream is = jarFile.getInputStream(file);
							FileOutputStream fos = new FileOutputStream(this.fileAchievements);
							while(is.available() > 0)
							{
								fos.write(is.read());
							}
							fos.close();
							is.close();
							VanillaMagic.logger.log(Level.INFO, "Achievements.json created");
							return;
						}
					}
				}
				else if(this.modFile.isDirectory())
				{
					File[] files = this.modFile.listFiles();
					for(File f : files)
					{
						if(f.getName().contains(achievements))
						{
							FileInputStream is = new FileInputStream(f);
							FileOutputStream fos = new FileOutputStream(this.fileAchievements);
							while(is.available() > 0)
							{
								fos.write(is.read());
							}
							fos.close();
							is.close();
							VanillaMagic.logger.log(Level.INFO, "Achievements.json created");
							return;
						}
					}
				}
			} 
			catch(IOException e) 
			{
				e.printStackTrace();
				VanillaMagic.logger.log(Level.INFO, "Error while creating Achievements.json");
			}
		}
	}
	
	public void readAchievements()
	{
		try 
		{
			FileInputStream fis = new FileInputStream(this.fileAchievements);
			InputStreamReader reader = new InputStreamReader(fis);
			JsonParser parser = new JsonParser();
			JsonElement js = parser.parse(reader);
			if(js.isJsonArray())
			{
				JsonArray ja = js.getAsJsonArray();
				for(JsonElement je : ja)
				{
					JsonObject jo = je.getAsJsonObject();
					Class<?> className = Class.forName(jo.get("questClass").getAsString());
					Quest quest = (Quest) className.newInstance();
					quest.readData(jo);
				}
				VanillaMagic.logger.log(Level.WARN, "VanillaMagic achievements readded from JSON file");
			}
			else
			{
				VanillaMagic.logger.log(Level.WARN, "Can't load VanillaMagic achievements from JSON file");
			}
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
			VanillaMagic.logger.log(Level.INFO, "Error while loading Achievements.json");
		}
	}
}