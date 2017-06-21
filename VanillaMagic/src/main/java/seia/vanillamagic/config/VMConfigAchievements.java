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
 */
public class VMConfigAchievements 
{
	public static final String VM_DIRECTORY = "/VanillaMagic/";
	
	private final String _achievements = "achievements.json";
	private final String _aboutAchievements = "About achievements.txt";
	
	private File _modConfigurationDirectory;
	private File _modFile;
	private File _fileAchievements;
	private File _fileAboutAchievements;
	
	/**
	 * modConfigurationDirectory - config/VanillaMagic/ <br>
	 * modFile - vanillamagic-[].jar OR /bin/ (if modding)
	 */
	public VMConfigAchievements(File modConfigurationDirectory, File modFile) 
	{
		this._modConfigurationDirectory = modConfigurationDirectory;
		this._modFile = modFile;
		if(!this._modConfigurationDirectory.exists())
		{
			this._modConfigurationDirectory.mkdirs();
			VanillaMagic.LOGGER.log(Level.INFO, "VanillaMagic config directory created");
		}
		VanillaMagic.LOGGER.log(Level.INFO, "VanillaMagic config directory loaded");
		_fileAchievements = unzip(_achievements, _fileAchievements);
		_fileAboutAchievements = unzip(_aboutAchievements, _fileAboutAchievements);
		readAchievements();
	}
	
	/**
	 * Unzip the specified file from JAR - copy into directory from inside of JAR file.
	 */
	@SuppressWarnings("resource")
	public File unzip(String fileName, File localFile) 
	{
		localFile = new File(_modConfigurationDirectory, fileName);
		if(!localFile.exists())
		{
			try 
			{
				localFile.createNewFile();
				if(!this._modFile.isDirectory())
				{
					JarFile jarFile = new JarFile(this._modFile);
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
				else if(this._modFile.isDirectory())
				{
					File[] files = this._modFile.listFiles();
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
	
	/**
	 * Read all achievements from achievements.json file
	 */
	public void readAchievements()
	{
		try 
		{
			FileInputStream fis = new FileInputStream(this._fileAchievements);
			InputStreamReader reader = new InputStreamReader(fis);
			JsonParser parser = new JsonParser();
			JsonElement js = parser.parse(reader);
			if(js.isJsonArray())
			{
				JsonArray ja = js.getAsJsonArray();
				for(JsonElement je : ja)
				{
					JsonObject jo = je.getAsJsonObject();
					Class<?> className = Class.forName(jo.get("questClass").getAsString()); // Read Quest class.
					IQuest quest = (IQuest) className.newInstance(); // Create Quest instance
					quest.readData(jo); // Read Quest data.
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