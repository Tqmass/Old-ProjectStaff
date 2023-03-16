package me.hqwks.creabyte.projectstaff.handlers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        reloadConfig();
    }

    public void reloadConfig() {
        File configFile = new File(plugin.getDataFolder(), "settings.yml");

        if (!configFile.exists()) {
            plugin.saveResource("settings.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        File configFile = new File(plugin.getDataFolder(), "settings.yml");

        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Error al intentar guardar la configuración: " + e.getMessage());
        }
    }
}