package com.leontg77.teaminv;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Settings class to manage the config and world files.
 * 
 * @author LeonTG
 */
public class Settings {
    private FileConfiguration config;
    private File cfile;

    Settings(Main plugin) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        cfile = new File(plugin.getDataFolder(), "config.yml");
        boolean creating = false;

        if (!cfile.exists()) {
            try {
                cfile.createNewFile();
                creating = true;
            } catch (Exception e) {
                plugin.getLogger().severe("Could not create config.yml!");
            }
        }

        reloadConfig();

        if (creating) {
            getConfig().set("rows", 4);
            saveConfig();
        }

        plugin.getLogger().info("Configs has been setup.");
    }

    /**
     * Gets the config file.
     *
     * @return The file.
     */
    public FileConfiguration getConfig() {
        return config;
    }
    
    /**
     * Saves the data config.
     */
    private void saveConfig() {
        try {
            config.save(cfile);
        } catch (Exception e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save config.yml!");
        }
    }
    
    /**
     * Reloads the config file.
     */
    private void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(cfile);
    }
}