package org.cptgummiball.eXVoucherExpansion;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatListener implements Listener {

    private final EXVoucherExpansion plugin;
    private final Map<String, Integer> globalKitLimits = new HashMap<>();
    private final Map<String, Map<String, Integer>> playerKitLimits = new HashMap<>();
    private File dataFile;
    private FileConfiguration dataConfig;

    public ChatListener(EXVoucherExpansion plugin) {
        this.plugin = plugin;
        loadDataFile();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();
        FileConfiguration config = plugin.getPluginConfig();

        // Check if message matches any keyword
        for (String keyword : config.getKeys(false)) {
            if (message.contains(keyword.toLowerCase())) {
                handleKeyword(player, keyword);
                break;
            }
        }
    }

    private void handleKeyword(Player player, String keyword) {
        FileConfiguration config = plugin.getPluginConfig();
        FileConfiguration langConfig = plugin.getLangConfig();

        // Get the translated messages from lang.yml
        String noKit = langConfig.getString("noKit", "No available kits for this keyword: ");
        String getKit = langConfig.getString("getKit", "You have been given the kit: ");
        String keywordInactive = langConfig.getString("keywordInactive", "This keyword is not active at the moment.");

        // Get the kit configuration
        List<String> kits = config.getStringList(keyword + ".Kits");
        int perPlayerLimit = config.getInt(keyword + ".perPlayerLimit");
        int globalLimit = config.getInt(keyword + ".GlobalLimit");

        // Parse start and end date from config
        String startDateString = config.getString(keyword + ".StartDate");
        String endDateString = config.getString(keyword + ".EndDate");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy, HH:mm:ss");

        try {
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);
            Date now = new Date();

            // Check if current date is within the allowed range
            if (now.before(startDate) || now.after(endDate)) {
                player.sendMessage(ChatColor.RED + keywordInactive);
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (String kitEntry : kits) {
            String[] parts = kitEntry.split(",");
            String kitName = parts[0].trim();
            int kitLimit = Integer.parseInt(parts[1].trim());

            // Check player's kit limit
            if (perPlayerLimit > 0 && getPlayerKitCount(player.getName(), kitName) >= perPlayerLimit) {
                continue;
            }

            // Check global kit limit
            if (kitLimit > 0 && getGlobalKitCount(kitName) >= kitLimit) {
                continue;
            }

            // Give the kit
            plugin.giveKit(player.getName(), kitName);

            // Update limits and save to file
            incrementPlayerKitCount(player.getName(), kitName);
            incrementGlobalKitCount(kitName);
            saveDataFile();

            player.sendMessage(ChatColor.GREEN + getKit + kitName);
            return;
        }

        player.sendMessage(ChatColor.RED + noKit + keyword);
    }

    // Get player's kit count from the data file
    private int getPlayerKitCount(String playerName, String kitName) {
        return dataConfig.getInt("players." + playerName + ".kits." + kitName, 0);
    }

    // Increment player's kit count and save to the data file
    private void incrementPlayerKitCount(String playerName, String kitName) {
        int currentCount = getPlayerKitCount(playerName, kitName);
        dataConfig.set("players." + playerName + ".kits." + kitName, currentCount + 1);
    }

    // Get global kit count from the data file
    private int getGlobalKitCount(String kitName) {
        return dataConfig.getInt("global.kits." + kitName, 0);
    }

    // Increment global kit count and save to the data file
    private void incrementGlobalKitCount(String kitName) {
        int currentCount = getGlobalKitCount(kitName);
        dataConfig.set("global.kits." + kitName, currentCount + 1);
    }

    // Load the data file containing player and global limits
    private void loadDataFile() {
        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    // Save the updated data back to the data file
    private void saveDataFile() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
