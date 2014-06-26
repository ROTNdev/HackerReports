package com.rotn.hackerreports;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class HR extends JavaPlugin {

    public static File dataFolder;
    public YamlConfiguration settings;
    public ReportHandler rh;
    public String prefix;

    public void onEnable() {
        Bukkit.getLogger().info("[HackerReports] Enabled version " + getDescription().getVersion() + "!");
        File settingsFile = new File(getDataFolder(), "settings.yml");
        if (!settingsFile.exists()) {
            settingsFile.getParentFile().mkdirs();
            saveResource("settings.yml", true);
        }
        settings = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "settings.yml"));
        prefix = settings.getString("prefix").replaceAll("&", "§") + " ";
        dataFolder = getDataFolder();
        File reportsFolder = new File(getDataFolder() + "/reports");
        if (!reportsFolder.exists()) {
            reportsFolder.mkdirs();
        }
        rh = new ReportHandler(reportsFolder);
        try {
            rh.loadAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getCommand("report").setExecutor(new CommandReport(this));
        getCommand("reports").setExecutor(new CommandReports(this));
        getServer().getPluginManager().registerEvents(new GUIDelete(this), this);
    }

}
