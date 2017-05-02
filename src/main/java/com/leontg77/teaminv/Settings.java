/*
 * Project: TeamInventory
 * Class: com.leontg77.teaminv.Settings
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Leon Vaktskjold <leontg77@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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