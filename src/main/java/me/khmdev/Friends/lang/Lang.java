package me.khmdev.Friends.lang;

import java.util.Locale;
import java.util.ResourceBundle;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import me.khmdev.APIBase.Almacenes.local.ConfigFile;

public class Lang {
	private static ConfigFile config = null;
	private static final String bumble=Lang.class.getPackage().getName()+".txt";
	public static void init(Plugin pl) {
		ResourceBundle lang = ResourceBundle.getBundle(bumble, new Locale("es", "ES"));

		config = new ConfigFile(pl.getDataFolder(), "lang");
		for (String k : lang.keySet()) {
			if(!config.getConfig().contains(k)){
				config.getConfig().set(k, lang.getString(k));
			}
		}
		ResourceBundle.clearCache();
		config.saveConfig();

	}

	public static String get(String s) {
		return ChatColor.translateAlternateColorCodes('&',config != null ? config.getConfig().getString(s) : "");
	}

}
