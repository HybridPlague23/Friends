package me.HybridPlague.Friends.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.HybridPlague.Friends.Main;

public class CommandHelp {

	private Main plugin;
	public CommandHelp(Main plugin) {
		this.plugin = plugin;
	}
	
	public void commandHelp(final Player p) {
		
		/* GET HEADER */
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Help-Messages-Header")));
		
		/* GET BASIC MESSAGES */
		for (int i = 0; i < plugin.getConfig().getStringList("Help-Messages").size(); i++) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getStringList("Help-Messages").toArray()[i].toString()));
		}
		
		/* GET RELOAD MESSAGE */
		if (p.hasPermission("friends.reload")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Help-Messages-Reload")));
		}
		
		/* GET FOOTER */
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Help-Messages-Footer")));
		
		return;
	}
	
}
