package me.HybridPlague.Friends.Commands;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.HybridPlague.Friends.Main;

public class CommandBlock {

	public YamlConfiguration playerConfig = new YamlConfiguration();
	public YamlConfiguration targetConfig = new YamlConfiguration();
	
	private Main plugin;
	public CommandBlock(Main plugin) {
		this.plugin = plugin;
	}
	
	public void commandBlock(final Player p, final OfflinePlayer target) {
		
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
		if (playerConfig.getStringList("Friends").contains(target.getUniqueId().toString())) {
			// Is a friend
			  // Remove from player's list
			try { 
				List<String> friends = playerConfig.getStringList("Friends");
				friends.remove(target.getUniqueId().toString());
				playerConfig.set("Friends", friends);
				playerConfig.set("Dates-Added." + target.getUniqueId().toString(), null);
				playerConfig.save(playerData);
			} catch (Exception ex) {
			}
			  // Remove from target's list
			try { 
				List<String> friends = targetConfig.getStringList("Friends");
				friends.remove(p.getUniqueId().toString());
				targetConfig.set("Friends", friends);
				targetConfig.set("Dates-Added." + p.getUniqueId().toString(), null);
				targetConfig.save(playerData2);
			} catch (Exception ex) {
			}
		}
		
		/* ADD TO BLOCK LIST */
		try {
			List<String> blocks = playerConfig.getStringList("Blocks");
			blocks.add(target.getUniqueId().toString());
			playerConfig.set("Blocks", blocks);
			
			/* GET + SET DATE BLOCKED */
			  // Get
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat(plugin.getConfig().getString("Date-Format"));
			String dateBlocked = format.format(now);
			
			  // Set
			playerConfig.set("Blocked-Dates." + target.getUniqueId().toString(), dateBlocked);
			playerConfig.save(playerData);
			
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Block.Blocked").replace("%p%", target.getName())));
		} catch (Exception ex) {
		}
		
		
	}
	
	
}
