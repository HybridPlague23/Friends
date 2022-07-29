package me.HybridPlague.Friends.Commands;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.HybridPlague.Friends.Main;

public class CommandAccept {

	
	public YamlConfiguration playerConfig = new YamlConfiguration();
	public YamlConfiguration targetConfig = new YamlConfiguration();
	
	private Main plugin;
	public CommandAccept(Main plugin) {
		this.plugin = plugin;
	}
	
	public void commandAccept(final Player p, final OfflinePlayer target) {
		
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
		
		/* DOES PLAYER HAVE AN INCOMING REQUEST FROM TARGET */
		if (!playerConfig.getStringList("Incoming-Requests").contains(target.getUniqueId().toString())) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.Accept.No-Request-From").replace("%p%", target.getName())));
			return;
		}
		
		/* DOES DATA EXIST ALREADY (prevents duplication) */
		if (playerConfig.getStringList("Friends").contains(target.getUniqueId().toString())) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.Accept.Already-Friends").replace("%p%", target.getName())));
			return;
		}
		
		/* NONE OF THE CONDITIONS ABOVE APPLY, CONTINUE */
		try {
			  // Remove UUID from incoming-requests list
			List<String> reqs = playerConfig.getStringList("Incoming-Requests");
			reqs.remove(target.getUniqueId().toString());
			playerConfig.set("Incoming-Requests", reqs);
			
			  // Add target to player's friend list
			List<String> friends = playerConfig.getStringList("Friends");
			friends.add(target.getUniqueId().toString());
			playerConfig.set("Friends", friends);
			
			  // Add player to target's friend list
			List<String> friends2 = targetConfig.getStringList("Friends");
			friends2.add(p.getUniqueId().toString());
			targetConfig.set("Friends", friends2);
			
			  // Send messages
			if (target.isOnline()) {
				Player newTarget = Bukkit.getPlayer(target.getUniqueId());
				newTarget.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Accept.Player-Accepted").replace("%p%", p.getName())));
			}
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Accept.Target-Accepted").replace("%p%", target.getName())));
			
			/* GET + SET DATE ADDED */
			  // Get
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat(plugin.getConfig().getString("Date-Format"));
			String dateAdded = format.format(now);
			
			  // Set for player config
			playerConfig.set("Added-Dates." + target.getUniqueId().toString(), dateAdded);
			  // Set for target config
			targetConfig.set("Added-Dates." + p.getUniqueId().toString(), dateAdded);
			
			playerConfig.save(playerData);
			targetConfig.save(playerData2);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		
		return;
	}
	
}
