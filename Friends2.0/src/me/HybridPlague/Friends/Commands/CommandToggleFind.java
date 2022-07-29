package me.HybridPlague.Friends.Commands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.HybridPlague.Friends.Main;

public class CommandToggleFind {

	public YamlConfiguration playerConfig = new YamlConfiguration();
	
	private Main plugin;
	public CommandToggleFind(Main plugin) {
		this.plugin = plugin;
	}
	
	public void commandToggleFind(final Player p) {
		File playerData = new File(plugin.getDataFolder(), "players/" + p.getUniqueId().toString() + ".yml");
		
		/* LOAD CONFIGS */
		
		try {
			playerConfig.load(playerData);
		} catch (Exception ex) {
		}
		
		/*-------------*/
		
		/* GET CURRENT SETTING */
		  // is on
		if (playerConfig.getString("Toggle.Find") == null || playerConfig.getString("Toggle.Find").equalsIgnoreCase("Off")) {
			try {
				playerConfig.set("Toggle.Find", "On");
				playerConfig.save(playerData);
			} catch (Exception ex) {
			}
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Toggle.Find-On")));
			return;
		}
		  // is off
		try {
			playerConfig.set("Toggle.Find", "Off");
			playerConfig.save(playerData);
		} catch (Exception ex) {
		}
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Toggle.Find-Off")));
		
		return;
	}
	
}
