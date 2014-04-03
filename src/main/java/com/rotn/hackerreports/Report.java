package com.rotn.hackerreports;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class Report {

	private String name;
	private List<String> reportCollection;
	
	public Report(String playername, List<String> reports){
		this.name = playername;
		this.reportCollection = reports;
	}
	
	public void save() throws IOException{
		YamlConfiguration report = new YamlConfiguration();
		report.set("Name", this.name);
		report.set("Reports", this.reportCollection);
		File file = new File(HR.dataFolder + File.separator + "reports", this.name + ".yml");
		report.save(file);
	}
	
	public void addReport(String report){
		reportCollection.add(report);
	}
	
	public String getName(){
		return this.name;
	}
	
	public List<String> getReports(){
		return this.reportCollection;
	}
	
	public int getTimesReports(){
		return this.reportCollection.size();
	}

	public void delete() throws IOException{
		File file = new File(HR.dataFolder + File.separator + "reports", this.name + ".yml");
		file.delete();
	}
	
}