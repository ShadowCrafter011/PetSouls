package org.shadowcrafter.petsouls.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigUtils {
	
	public static Object validatePath(JavaPlugin main, String path, Object defaultValue) {
		
		FileConfiguration config = main.getConfig();
		
		if (!config.isSet(path)) {
			config.set(path, defaultValue);
			main.saveConfig();
			return defaultValue;
		}else {
			return config.get(path);
		}
	}

}
