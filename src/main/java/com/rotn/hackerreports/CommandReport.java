package com.rotn.hackerreports;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReport implements CommandExecutor{

	private HR plugin;

	public CommandReport(HR plugin){
		this.plugin = plugin;
	}

	private String getMessage(String[] args, int start) {
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = start; i < args.length; i++) {
			if (i != start) {
				stringBuilder.append(" ");
			}

			stringBuilder.append(args[i]);
		}

		return stringBuilder.toString().replace('&', '§');
	}

	private String getTime(){
    	Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(cal.getTime()) + ": ";
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(player.hasPermission("hackerreports.report")){
				Player target = Bukkit.getPlayer(args[0]);
				if(target != null){
					if(args.length > 1){
						String message = getMessage(args, 1);
						for(Player pl : Bukkit.getOnlinePlayers()){
							if(pl.hasPermission("hackerreports.see"))
								pl.sendMessage(plugin.prefix + ChatColor.DARK_AQUA + player.getName() + " has reported " + ChatColor.DARK_AQUA + target.getName() + " for: " + ChatColor.GREEN + message);
						}
						player.sendMessage(plugin.prefix + ChatColor.DARK_AQUA + "Report submitted to online staff.");
						if(plugin.rh.isReport(args[0])){
							Report report = plugin.rh.getReport(args[0]);
							report.addReport(this.getTime() + player.getName() + " reported player for: " + message);
							try {
								report.save();
							} catch (IOException e) {
								e.printStackTrace();
							}
							plugin.rh.updateReport(args[0], report);
						}else{
							List<String> reportCollection = new ArrayList<String>();
							reportCollection.add(this.getTime() + player.getName() + " reported player for: " + message);
							Report report = new Report(
									args[0], 
									reportCollection);
							try {
								report.save();
							} catch (IOException e) {
								e.printStackTrace();
							}
							plugin.rh.updateReport(args[0], report);
						}
					}else{
						player.sendMessage(plugin.prefix + ChatColor.RED + "Please specify a reason.");
					}
				}else{
					OfflinePlayer offlinetarget = Bukkit.getOfflinePlayer(args[0]);
					if(offlinetarget != null){
						if(args.length > 1){
							String message = getMessage(args, 1);
							for(Player pl : Bukkit.getOnlinePlayers()){
								if(pl.hasPermission("hackerreports.see"))
									pl.sendMessage(plugin.prefix + ChatColor.DARK_AQUA + player.getName() + " has reported " + ChatColor.DARK_AQUA + offlinetarget.getName() + " for: " + ChatColor.GREEN + message);
							}
							if(plugin.rh.isReport(args[0])){
								Report report = plugin.rh.getReport(args[0]);
								report.addReport(this.getTime() + player.getName() + " reported player for: " + message);
								try {
									report.save();
								} catch (IOException e) {
									e.printStackTrace();
								}
								plugin.rh.updateReport(args[0], report);
							}else{
								List<String> reportCollection = new ArrayList<String>();
								reportCollection.add(this.getTime() + player.getName() + " reported player for: " + message);
								Report report = new Report(args[0], reportCollection);
								try {
									report.save();
								} catch (IOException e) {
									e.printStackTrace();
								}
								plugin.rh.updateReport(args[0], report);
							}
						}else{
							player.sendMessage(plugin.prefix + ChatColor.RED + "Please specify a reason.");
						}
					}else{
						player.sendMessage(plugin.prefix + ChatColor.RED + "Player not found.");
					}
				}
			}
		}
		return true;
	}

}
