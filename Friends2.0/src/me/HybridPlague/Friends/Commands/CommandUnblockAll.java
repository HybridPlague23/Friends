package me.HybridPlague.Friends.Commands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.HybridPlague.Friends.Main;

public class CommandUnblockAll {

	public YamlConfiguration playerConfig = new YamlConfiguration();
	
	private Main plugin;
	public CommandUnblockAll(Main plugin) {
		this.plugin = plugin;
	}
	
	public void commandUnblockAll(final Player p) {
		
		File playerData = new File(plugin.getDataFolder(), "players/" + p.getUniqueId().toString() + ".yml");
		
		/* LOAD CONFIGS */
		
		try {
			playerConfig.load(playerData);
		} catch (Exception ex) {
		}
		
		/*-------------*/
		
		/* DOES PLAYER HAVE BLOCKS */
		if (playerConfig.getStringList("Blocks").size() == 0 || playerConfig.getStringList("Blocks") == null) {
			
			// Does not have blocks
			
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.UnblockAll.No-Blocks")));
			return;
			
		}
		
		// Has blocks
		  // Clear list
		try {
			playerConfig.set("Blocks", null);
			playerConfig.set("Blocked-Dates", null);
			playerConfig.save(playerData);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.UnblockAll.Unblocked")));
		} catch (Exception ex) {
		}
	}
	
	
}
