package com.rotn.hackerreports;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class Report {

    private String name;
    private List<String> reportCollection;
    private UUID uuid;

    public Report(OfflinePlayer player, List<String> reports) {
        this.name = player.getName();
        this.reportCollection = reports;
        this.uuid = player.getUniqueId();
    }

    public Report(String name, UUID uuid, List<String> reports) {
        this.name = name;
        this.reportCollection = reports;
        this.uuid = uuid;
    }

    public void save() throws IOException {
        YamlConfiguration report = new YamlConfiguration();
        report.set("Name", this.name);
        report.set("UUID", this.uuid.toString());
        report.set("Reports", this.reportCollection);
        File file = new File(HR.dataFolder + File.separator + "reports", this.uuid.toString() + ".yml");
        report.save(file);
    }

    public void addReport(String report) {
        reportCollection.add(report);
    }

    public String getName() {
        return this.name;
    }

    public List<String> getReports() {
        return this.reportCollection;
    }

    public int getTimesReports() {
        return this.reportCollection.size();
    }

    public void delete() throws IOException {
        File file = new File(HR.dataFolder + File.separator + "reports", this.name + ".yml");
        file.delete();
    }

}