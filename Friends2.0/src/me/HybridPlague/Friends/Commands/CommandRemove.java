package me.HybridPlague.Friends.Commands;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.HybridPlague.Friends.Main;

public class CommandRemove {
	
	public YamlConfiguration playerConfig = new YamlConfiguration();
	public YamlConfiguration targetConfig = new YamlConfiguration();
	
	private Main plugin;
	public CommandRemove(Main plugin) {
		this.plugin = plugin;
	}
	
	public void commandRemove(final Player p, final OfflinePlayer target) {
		
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
		
		/*-------------*/
		
		/* IS TARGET SELF */
		if (p.equals(target)) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.General.Self-Target")));
			return;
		}
		
		/* IS TARGET A FRIEND */
		if (!playerConfig.getStringList("Friends").contains(target.getUniqueId().toString())) {
			// Not a friend
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.Remove.Not-Friends").replace("%p%", target.getName())));
			return;
		}
		// Is a friend
		  // Remove from player's list
		try {
			List<String> friends = playerConfig.getStringList("Friends");
			friends.remove(target.getUniqueId().toString());
			playerConfig.set("Friends", friends);
			playerConfig.set("Added-Dates." + target.getUniqueId().toString(), null);
			playerConfig.save(playerData);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Remove.Removed").replace("%p%", target.getName())));
		} catch (Exception ex) {
			System.out.println(ChatColor.RED + "Error saving data file for " + p.getName());
		}
		  // Remove from target's list
		try {
			List<String> friends = targetConfig.getStringList("Friends");
			friends.remove(p.getUniqueId().toString());
			targetConfig.set("Friends", friends);
			targetConfig.set("Added-Dates." + p.getUniqueId().toString(), null);
			targetConfig.save(playerData2);
		} catch (Exception ex) {
			System.out.println(ChatColor.RED + "Error saving data file for " + target.getName());
		}
		
		return;
	}
	
}
