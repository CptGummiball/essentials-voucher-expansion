package org.cptgummiball.eXVoucherExpansion;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class EXVoucherExpansion extends JavaPlugin {

    private FileConfiguration config;
    private FileConfiguration langConfig;

    @Override
    public void onEnable() {
        // Load configuration
        saveDefaultConfig();
        this.config = getConfig();
        loadLangFile();
        saveResource("data.yml", false);

        // Register ChatListener
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        getLogger().info("Essentials Voucher Expansion has been enabled!");
    }

    public FileConfiguration getPluginConfig() {
        return config;
    }

    public void giveKit(String playerName, String kitName) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        String command = "kit " + kitName + " " + playerName;
        Bukkit.dispatchCommand(console, command);
    }

    // Load the lang.yml configuration file
    public void loadLangFile() {
        File langFile = new File(getDataFolder(), "lang.yml");

        if (!langFile.exists()) {
            langFile.getParentFile().mkdirs();
            saveResource("lang.yml", false);
        }

        langConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    public FileConfiguration getLangConfig() {
        return langConfig;
    }
}
