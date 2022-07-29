package me.HybridPlague.Friends.Commands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.HybridPlague.Friends.Main;

public class CommandFind {

	public YamlConfiguration playerConfig = new YamlConfiguration();
	public YamlConfiguration targetConfig = new YamlConfiguration();
	
	private Main plugin;
	
	public CommandFind(Main plugin) {
		this.plugin = plugin;
	}
	
	public void commandFind(final Player p, final Player target) {
		
		File playerData = new File(plugin.getDataFolder(), "players/" + p.getUniqueId().toString() + ".yml");
		File playerData2 = new File(plugin.getDataFolder(), "players/" + target.getUniqueId().toString() + ".yml");
		
		/* LOAD CONFIGS */
		
		try {
			playerConfig.load(playerData);
		} catch (Exception ex) {
		}
		
		try {
			targetConfig.load(playerData2);
		} catch (Exception ex) {
		}
		
		if (!target.isOnline()) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.General.Unknown-Player")));
			return;
		}
		
		if (!playerConfig.getStringList("Friends").contains(target.getUniqueId().toString())) {
			// Not a friend
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.Find.Not-Friends").replace("%p%", target.getName())));
			return;
		}
		
		if (targetConfig.getString("Toggle.Find").equals("Off")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.Find.Toggled-Off").replace("%p%", target.getName())));
			return;
		}
		
		Location loc = target.getLocation();
		String world = target.getWorld().getName();
		
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Find").replace("%x%", String.valueOf(Math.round(x)))
				.replace("%y%", String.valueOf(Math.round(y)))
				.replace("%z%", String.valueOf(Math.round(z)))
				.replace("%world%", world))
				.replace("%p%", target.getName()));
		
	}
	
}
