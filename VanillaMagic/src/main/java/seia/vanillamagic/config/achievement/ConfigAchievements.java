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
import seia.vanillamagic.quest.IQuest;

public class ConfigAchievements 
{
	public static final String VM_DIRECTORY = "/VanillaMagic/";
	
	public File modConfigurationDirectory;
	public File modFile;
	
	private final String achievements = "achievements.json";
	private final String aboutAchievements = "About achievements.txt";
	
	private File fileAchievements;
	private File fileAboutAchievements;
	
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
			VanillaMagic.LOGGER.log(Level.INFO, "VanillaMagic config directory created");
		}
		VanillaMagic.LOGGER.log(Level.INFO, "VanillaMagic config directory loaded");
		fileAchievements = unzip(achievements, fileAchievements);
		fileAboutAchievements = unzip(aboutAchievements, fileAboutAchievements);
		readAchievements();
	}

	@SuppressWarnings("resource")
	public File unzip(String fileName, File localFile) 
	{
		localFile = new File(modConfigurationDirectory, fileName);
		if(!localFile.exists())
		{
			try 
			{
				localFile.createNewFile();
				if(!this.modFile.isDirectory())
				{
					JarFile jarFile = new JarFile(this.modFile);
					Enumeration enumeration = jarFile.entries();
					while(enumeration.hasMoreElements())
					{
						JarEntry file = (JarEntry) enumeration.nextElement();
						if(file.getName().equals(fileName))
						{
							InputStream is = jarFile.getInputStream(file);
							FileOutputStream fos = new FileOutputStream(localFile);
							while(is.available() > 0)
							{
								fos.write(is.read());
							}
							fos.close();
							is.close();
							VanillaMagic.LOGGER.log(Level.INFO, fileName + " created");
							return localFile;
						}
					}
				}
				else if(this.modFile.isDirectory())
				{
					File[] files = this.modFile.listFiles();
					for(File f : files)
					{
						if(f.getName().contains(fileName))
						{
							FileInputStream is = new FileInputStream(f);
							FileOutputStream fos = new FileOutputStream(localFile);
							while(is.available() > 0)
							{
								fos.write(is.read());
							}
							fos.close();
							is.close();
							VanillaMagic.LOGGER.log(Level.INFO, fileName + " created");
							return localFile;
						}
					}
				}
			} 
			catch(IOException e) 
			{
				e.printStackTrace();
				VanillaMagic.LOGGER.log(Level.INFO, "Error while creating " + fileName);
			}
		}
		return localFile;
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
					IQuest quest = (IQuest) className.newInstance();
					quest.readData(jo);
				}
				VanillaMagic.LOGGER.log(Level.WARN, "VanillaMagic achievements readded from JSON file");
			}
			else
			{
				VanillaMagic.LOGGER.log(Level.WARN, "Can't load VanillaMagic achievements from JSON file");
			}
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
			VanillaMagic.LOGGER.log(Level.INFO, "Error while loading Achievements.json");
		}
	}
}