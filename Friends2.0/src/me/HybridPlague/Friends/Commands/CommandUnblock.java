package me.HybridPlague.Friends.Commands;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.HybridPlague.Friends.Main;

public class CommandUnblock {

	public YamlConfiguration playerConfig = new YamlConfiguration();
	
	private Main plugin;
	public CommandUnblock(Main plugin) {
		this.plugin = plugin;
	}
	
	public void commandUnblock(final Player p, final OfflinePlayer target) {
		
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
		
		
		/* IS TARGET BLOCKED */
		if (!playerConfig.getStringList("Blocks").contains(target.getUniqueId().toString())) {
			// Not blocked
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.Unblock.Not-Blocked").replace("%p%", target.getName())));
			return;
		}
		
		  // Is blocked
		try {
			List<String> blocks = playerConfig.getStringList("Blocks");
			blocks.remove(target.getUniqueId().toString());
			playerConfig.set("Blocks", blocks);
			playerConfig.set("Blocked-Dates." + target.getUniqueId().toString(), null); 
			playerConfig.save(playerData);
			
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Unblock.Unblocked").replace("%p%", target.getName())));
		} catch (Exception ex) {
		}
		
	}
	
}
