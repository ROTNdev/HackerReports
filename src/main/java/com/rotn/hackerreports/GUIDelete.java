package com.rotn.hackerreports;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import java.io.IOException;

public class GUIDelete implements Listener{

	private HR plugin;

	public GUIDelete(HR plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onDeleteInvClose(InventoryCloseEvent event){
		if(event.getInventory().getName().contains( ChatColor.RED + "Delete Reports")){
			Player player = (Player) event.getPlayer();
			StringBuilder deletedReports = new StringBuilder();
            boolean hasDeletedAtLeastOne = false;
			for(ItemStack item : event.getInventory().getContents()) {
				if (item != null) {
					if(item.getType().equals(Material.WRITTEN_BOOK))
					if (item.hasItemMeta()) {
						String itemname = item.getItemMeta().getDisplayName();
						if (itemname.endsWith("'s hacker reports.")) {
							String reportName = itemname.substring(2, itemname.length() - 20);
							if (plugin.rh.isReport(reportName)) {
								Report report = plugin.rh.getReport(reportName);
								if(deletedReports.length() > 0)deletedReports.append(", ");
								deletedReports.append(ChatColor.GOLD + report.getName() + ChatColor.GRAY);
								try {
									report.delete();
                                    hasDeletedAtLeastOne = true;
								} catch (IOException e) {
									e.printStackTrace();
								}
								plugin.rh.removeReport(reportName);
							}
						}
					}
				}
			}
            if(hasDeletedAtLeastOne) {
                player.sendMessage(this.plugin.prefix + ChatColor.AQUA + "Deleted the report data for; " + deletedReports.toString());
            } else {
                player.sendMessage(this.plugin.prefix + ChatColor.RED + "No reports were found in the inventory.");
            }
		}
	}

}
