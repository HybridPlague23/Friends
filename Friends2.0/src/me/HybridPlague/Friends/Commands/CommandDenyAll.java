package me.HybridPlague.Friends.Commands;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.HybridPlague.Friends.Main;

public class CommandDenyAll {
	
	public YamlConfiguration playerConfig = new YamlConfiguration();
	
	private Main plugin;
	public CommandDenyAll(Main plugin) {
		this.plugin = plugin;
	}
	
	public void commandDenyAll(final Player p) {
		
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
			
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.DenyAll.No-Requests")));
			return;
			
		}
		
		  // Has requests
		for (int i = playerConfig.getStringList("Incoming-Requests").size(); i > 0; i--) {
			
			UUID request = UUID.fromString(playerConfig.getStringList("Incoming-Requests").toArray()[i - 1].toString()); // Get UUID
			
			// Send message
			try {
				Player r = Bukkit.getPlayer(request);
				r.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Deny.Player-Denied").replace("%p%", p.getName())));
			} catch (Exception ex) {
				
			}
		}
		
		// Delete incoming list
		try {
			playerConfig.set("Incoming-Requests", null);
			playerConfig.save(playerData);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.DenyAll.Denied")));
		} catch (Exception ex) {
		}
		
	}
	
}
