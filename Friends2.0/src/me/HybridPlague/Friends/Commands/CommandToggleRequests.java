package me.HybridPlague.Friends.Commands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.HybridPlague.Friends.Main;

public class CommandToggleRequests {

	public YamlConfiguration playerConfig = new YamlConfiguration();
	
	private Main plugin;
	public CommandToggleRequests(Main plugin) {
		this.plugin = plugin;
	}
	
	public void commandToggle(final Player p) {
		
		File playerData = new File(plugin.getDataFolder(), "players/" + p.getUniqueId().toString() + ".yml");
		
		/* LOAD CONFIGS */
		
		try {
			playerConfig.load(playerData);
		} catch (Exception ex) {
		}
		
		/*-------------*/
		
		/* GET CURRENT SETTING */
		  // is on
		if (playerConfig.getString("Toggle.Requests") == null || playerConfig.getString("Toggle.Requests").equalsIgnoreCase("On")) {
			try {
				playerConfig.set("Toggle.Requests", "Off");
				playerConfig.save(playerData);
			} catch (Exception ex) {
			}
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Toggle.Requests-Off")));
			return;
		}
		  // is off
		try {
			playerConfig.set("Toggle.Requests", "On");
			playerConfig.save(playerData);
		} catch (Exception ex) {
		}
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Toggle.Requests-On")));
		
		return;
	}
	
	
}
