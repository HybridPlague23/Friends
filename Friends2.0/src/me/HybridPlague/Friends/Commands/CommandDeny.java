package me.HybridPlague.Friends.Commands;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.HybridPlague.Friends.Main;

public class CommandDeny {

	public YamlConfiguration playerConfig = new YamlConfiguration();
	
	private Main plugin;
	public CommandDeny(Main plugin) {
		this.plugin = plugin;
	}
	
	
	public void commandDeny(final Player p, final OfflinePlayer target) {
		
		File playerData = new File(plugin.getDataFolder(), "players/" + p.getUniqueId().toString() + ".yml");
		
		/* LOAD CONFIGS */
		
		try {
			playerConfig.load(playerData);
		} catch (Exception ex) {
		}
		
		/*-------------*/
		
		/* IS TARGET SELF */
		if (p.equals(target)) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.General.Self-Target")));
			return;
		}
		
		/* DOES PLAYER HAVE AN INCOMING REQUEST FROM TARGET */
		if (playerConfig.getStringList("Incoming-Requests").contains(target.getUniqueId().toString())) {
			
			// Remove from list
			try {
				List<String> incoming = playerConfig.getStringList("Incoming-Requests");
				incoming.remove(target.getUniqueId().toString());
				playerConfig.set("Incoming-Requests", incoming);
				playerConfig.save(playerData);
			} catch (Exception ex) {
			}
			
			// Send messages
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Deny.Target-Denied").replace("%p%", target.getName())));
			return;
		}
		  // No request
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.Deny.No-Request-From").replace("%p%", target.getName())));
		return;
		
	}
	
}
