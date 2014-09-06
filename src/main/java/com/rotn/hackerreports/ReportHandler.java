package com.rotn.hackerreports;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ReportHandler {

    private File dir;
    private Map<String, Report> reports = new HashMap<>();

    public ReportHandler(File dir) {
        this.dir = dir;
    }


    public void loadAll() throws IOException {
        Integer count = 0;
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                if (file.getCanonicalPath().endsWith(".yml")) {
                    YamlConfiguration report = YamlConfiguration.loadConfiguration(file);
                    String name = report.getString("Name");
                    List<String> reports = report.getStringList("Reports");
                    this.reports.put(name, new Report(name, UUID.fromString(report.getString("UUID")), reports));
                    count++;
                }
            }
        }
        System.out.println("[HackerReports] Loaded " + String.valueOf(count) + " report files!");
    }

    public Boolean isReport(String reportName) {
        return reports.containsKey(reportName);
    }

    public Collection<Report> getReports() {
        return reports.values();
    }

    public Report getReport(String reportName) {
        return reports.get(reportName);
    }

    public void updateReport(String reportName, Report report) {
        if (report == null) {
            reports.remove(reportName);
        } else {
            reports.put(reportName, report);
        }
    }

    public void removeReport(String reportName) {
        if (reports.containsKey(reportName))
            reports.remove(reportName);
    }
}
