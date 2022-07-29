package me.HybridPlague.Friends.Commands;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.HybridPlague.Friends.Main;

public class CommandAcceptAll {
	
	public YamlConfiguration playerConfig = new YamlConfiguration();
	public YamlConfiguration otherConfig = new YamlConfiguration();
	
	private Main plugin;
	public CommandAcceptAll(Main plugin) {
		this.plugin = plugin;
	}
	
	public void commandAcceptAll(final Player p) {
		
		File playerData = new File(plugin.getDataFolder(), "players/" + p.getUniqueId().toString() + ".yml");
		
		/* LOAD CONFIGS */
		
		try {
			playerConfig.load(playerData);
		} catch (Exception ex) {
		}
		
		/*-------------*/
		
		/* DOES PLAYER HAVE REQUESTS */
		if (playerConfig.getStringList("Incoming-Requests").size() == 0 || playerConfig.getStringList("Incoming-Requests") == null) {
			
			// Does not have requests
			
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.AcceptAll.No-Requests")));
			return;
			
		}
		
		// Has requests
		for (int i = playerConfig.getStringList("Incoming-Requests").size(); i > 0; i--) {
			
			// Find player config
			UUID friend = UUID.fromString(playerConfig.getStringList("Incoming-Requests").toArray()[i - 1].toString()); // Get UUID
			File otherData = new File(plugin.getDataFolder(), "players/" + friend + ".yml"); // Locate file
			
			  // Load file
			try {
				otherConfig.load(otherData);
			} catch (Exception ex) {
				System.out.println(ChatColor.RED + "Error loading data file for " + Bukkit.getOfflinePlayer(friend).getName()); 
			}
			
			  // Add player to file; other's config
			try { 
				List<String> friends = otherConfig.getStringList("Friends");
				friends.add(p.getUniqueId().toString());
				otherConfig.set("Friends", friends);
				otherConfig.save(otherData);
			} catch (Exception ex) {
				System.out.println(ChatColor.RED + "Error saving data file for " + Bukkit.getOfflinePlayer(friend).getName()); 
			}
			
			  // Accept all incoming requests; player's config
			UUID request = UUID.fromString(playerConfig.getStringList("Incoming-Requests").toArray()[i - 1].toString()); // Get UUID
			try {
				// Add UUID to friends list
				List<String> friends = playerConfig.getStringList("Friends");
				friends.add(request.toString());
				playerConfig.set("Friends", friends);
				playerConfig.save(playerData);
				
				// Remove UUID from incoming requests list
				List<String> requests = playerConfig.getStringList("Incoming-Requests");
				requests.remove(request.toString());
				playerConfig.set("Incoming-Requests", requests);
				playerConfig.save(playerData);
				
				
				// Send message to player
				Player f = Bukkit.getPlayer(friend);
				f.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Accept.Player-Accepted").replace("%p%", p.getName())));
			} catch (Exception ex) {
				
			}
			
			/* GET + SET DATE ADDED */
			  // Get
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat(plugin.getConfig().getString("Date-Format"));
			String dateAdded = format.format(now);
			
			try {
			  // Set for player config
			playerConfig.set("Added-Dates." + friend, dateAdded);
			  // Set for target config
			otherConfig.set("Added-Dates." + p.getUniqueId().toString(), dateAdded);
			
			playerConfig.save(playerData);
			otherConfig.save(otherData);
			} catch (Exception ex) {
			}
			
		}
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.AcceptAll.Accepted")));
		
		return;
		
	}
	
}
