package me.HybridPlague.Friends.Commands;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.HybridPlague.Friends.Main;

public class CommandRemoveAll {
	
	public YamlConfiguration playerConfig = new YamlConfiguration();
	public YamlConfiguration otherConfig = new YamlConfiguration();

	private Main plugin;
	public CommandRemoveAll(Main plugin) {
		this.plugin = plugin;
	}
	
	public void commandRemoveAll(final Player p) {
		
		File playerData = new File(plugin.getDataFolder(), "players/" + p.getUniqueId().toString() + ".yml");/* LOAD CONFIGS */

		/* LOAD CONFIG */
		
		try {
			playerConfig.load(playerData);
		} catch (Exception ex) {
		}
		
		/*-------------*/
		
		/* DOES PLAYER HAVE FRIENDS */
		if (playerConfig.getStringList("Friends").size() == 0 || playerConfig.getStringList("Friends") == null) {
			// No friends
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.RemoveAll.No-Friends")));
			return;
		}
		
		// Has friends
		
		  // Remove player from other lists
		for (int i = playerConfig.getStringList("Friends").size(); i > 0; i--) {
			
			// Find player config
			UUID friend = UUID.fromString(playerConfig.getStringList("Friends").toArray()[i - 1].toString()); // Get UUID
			File otherData = new File(plugin.getDataFolder(), "players/" + friend + ".yml"); // Locate file
			
			  // Load file
			try {
				otherConfig.load(otherData);
			} catch (Exception ex) {
				System.out.println(ChatColor.RED + "Error loading data file for " + Bukkit.getOfflinePlayer(friend).getName()); 
			}
			
			  // Remove player from file
			try { 
				List<String> friends = otherConfig.getStringList("Friends");
				friends.remove(p.getUniqueId().toString());
				otherConfig.set("Friends", friends);
				otherConfig.set("Added-Dates." + p.getUniqueId().toString(), null);
				otherConfig.save(otherData);
			} catch (Exception ex) {
				System.out.println(ChatColor.RED + "Error saving data file for " + Bukkit.getOfflinePlayer(friend).getName()); 
			}
			
		}
		try {
			playerConfig.set("Friends", null);
			playerConfig.set("Added-Dates", null);
			playerConfig.save(playerData);
		} catch (Exception ex) {
			System.out.println(ChatColor.RED + "Error saving data file for " + p.getName());
		}
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.RemoveAll.Removed")));
		
		return;
	}
	
	
}
