package com.rotn.hackerreports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class CommandReports implements CommandExecutor{

	private HR plugin;

	public CommandReports(HR plugin){
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(player.hasPermission("hackerreports.admin")){
				if(args.length ==0){
					player.sendMessage(plugin.prefix + ChatColor.WHITE + "Hacker Reports version " + plugin.getDescription().getVersion() + " by ROTN");
				}else if(args.length == 1){
                    if(args[0].equalsIgnoreCase("delete")){
                        if(player.hasPermission("hackerreports.delete")){
                            Inventory inv = Bukkit.createInventory(player, 18, ChatColor.RED + "Delete Reports");
                            player.openInventory(inv);
                        }
                    } else if(args[0].equalsIgnoreCase("list")){
						player.sendMessage(plugin.prefix + ChatColor.DARK_AQUA + "Listing reported players...");
                        Integer count = 0;
						StringBuilder reports = new StringBuilder();
						Collection<Report> reportCollection = plugin.rh.getReports();
                        if(reportCollection.size() != 0) {
                            for(Report report : reportCollection){
                                count++;
                                if(reports.length() > 0)reports.append(", ");
                                reports.append(ChatColor.GOLD + report.getName() + ChatColor.GRAY);
                            }
                        } else {
                            reports.append(ChatColor.RED + "No reports have been found.");
                            count = 0;
                        }
                        player.sendMessage(plugin.prefix + ChatColor.AQUA + "Found " + String.valueOf(count) + " reports.");
						player.sendMessage(plugin.prefix + reports.toString());
					}else if(args[0].equalsIgnoreCase("get")){
						player.sendMessage(plugin.prefix + ChatColor.RED + "Usage: /reports get <player>");
					}
					else if(args[0].equalsIgnoreCase("help")){
						player.sendMessage(plugin.prefix + ChatColor.GREEN + "Hacker Reports >> Help Page");
						player.sendMessage(plugin.prefix + ChatColor.GOLD + "/reports get <player>");
						player.sendMessage(plugin.prefix + ChatColor.GOLD + "/reports delete <player>");
                        player.sendMessage(plugin.prefix + ChatColor.GOLD + "/reports delete");
						player.sendMessage(plugin.prefix + ChatColor.GOLD + "/reports list");
					}
					else if(args[0].equalsIgnoreCase("getall")){
						//TODO Get all books
						Collection<Report> reportCollection = plugin.rh.getReports();
						Integer size = Math.round(reportCollection.size() / 9) * 9 + 9;
                        if(reportCollection.size() == 8)
                            size = 9;
						Inventory inv = Bukkit.createInventory(player, size, ChatColor.AQUA + "Hacker Reports");
						for(Report report : reportCollection){
							ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
							BookMeta meta = (BookMeta) book.getItemMeta();
							meta.setTitle(ChatColor.RED + report.getName() + "'s hacker reports.");
							meta.setDisplayName(ChatColor.AQUA + report.getName() + ChatColor.GREEN + "'s hacker reports.");
							for(String page : report.getReports()){
								meta.addPage(page);
							}
							meta.setAuthor(ChatColor.DARK_AQUA + "Hacker Reports");
							book.setItemMeta(meta);
							inv.addItem(book);
						}
                        player.openInventory(inv);
						player.sendMessage(plugin.prefix + ChatColor.DARK_AQUA + "Here are all the hacker reports.");
					}else if(args[0].equalsIgnoreCase("deleteall")){
						Collection<Report> reportCollection = plugin.rh.getReports();
						List<Report> reports = new ArrayList<>(reportCollection);
						Collections.reverse(reports);
						for(Report report : reports){
							try {
								report.delete();
								plugin.rh.removeReport(report.getName());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						player.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Deleted report data for all players.");
					}else{
						player.sendMessage(plugin.prefix + ChatColor.GOLD + "Unknown request. Type /reports help for a list off all possible commands.");
					}
				}else if(args.length == 2){
					if(args[0].equalsIgnoreCase("get")){
						if(plugin.rh.isReport(args[1])){
							player.sendMessage(plugin.prefix + ChatColor.DARK_AQUA + "Fetching report data for " + ChatColor.GOLD + args[1] + ChatColor.DARK_AQUA +  ".");

							Report report = plugin.rh.getReport(args[1]);
							ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
							BookMeta meta = (BookMeta) book.getItemMeta();
							meta.setTitle(ChatColor.RED + args[1] + "'s hacker reports.");
							meta.setDisplayName(ChatColor.AQUA + args[1] + ChatColor.GREEN + "'s hacker reports.");
							for(String page : report.getReports()){
								meta.addPage(page);
							}
							meta.setAuthor(ChatColor.DARK_AQUA + "Hacker Reports");
							book.setItemMeta(meta);
							player.getInventory().addItem(book);
							player.updateInventory();
						}else{
							player.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Report not found!");
						}
					}
					else if(args[0].equalsIgnoreCase("delete")){
						if(player.hasPermission("hackerreports.delete")){
							if(plugin.rh.isReport(args[1])){
								Report report = plugin.rh.getReport(args[1]);
								try {
									report.delete();
								} catch (IOException e) {
									e.printStackTrace();
								}
								plugin.rh.removeReport(args[1]);
								player.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Deleted report data for " + args[1] + ".");
							}else{
								player.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Report not found!");
							}
						}else{
							player.sendMessage(plugin.prefix + ChatColor.GOLD + "You don't have permission for this. Please ask an admin for assistance.");
						}
					}
					else{
						player.sendMessage(plugin.prefix + ChatColor.GOLD + "Unknown request. Type /reports help for a list off all possible commands.");
					}
				}else{
					player.sendMessage(plugin.prefix + ChatColor.GOLD + "Unknown request. Type /reports help for a list off all possible commands.");
				}
			}else{
				player.sendMessage(plugin.prefix + ChatColor.WHITE + plugin.getDescription().getVersion() + " by ROTN");
			}
		}
		return true;
	}

}
