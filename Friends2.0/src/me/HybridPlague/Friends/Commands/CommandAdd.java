package me.HybridPlague.Friends.Commands;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.HybridPlague.Friends.Main;

public class CommandAdd {

	public YamlConfiguration playerConfig = new YamlConfiguration();
	public YamlConfiguration targetConfig = new YamlConfiguration();
	
	private Main plugin;
	public CommandAdd(Main plugin) {
		this.plugin = plugin;
	}
	
	public void commandAdd(final Player p, final Player target) {
		
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
		
		/* IS TARGET BLOCKED */
		if (playerConfig.getStringList("Blocks").contains(target.getUniqueId().toString())) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.Add.Target-Blocked").replace("%p%", target.getName())));
			return;
		}
		
		/* IS PLAYER BLOCKED */
		if (targetConfig.getStringList("Blocks").contains(p.getUniqueId().toString())) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.Add.Player-Blocked").replace("%p%", target.getName())));
			return;
		}
		
		/* IS TARGET ACCEPTING FRIEND REQUESTS */
		if (targetConfig.getString("Toggle.Requests").equals(null) || !targetConfig.getString("Toggle.Requests").equalsIgnoreCase("On")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.Add.Not-Accepting").replace("%p%", target.getName())));
			return;
		}
		
		/* DOES TARGET ALREADY HAVE AN INCOMING REQUEST FROM PLAYER */
		if (targetConfig.getStringList("Incoming-Requests").contains(p.getUniqueId().toString())) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.Add.Already-Sent").replace("%p%", target.getName())));
			return;
		}
		
		/* IS TARGET ALREADY A FRIEND */
		if (playerConfig.getStringList("Friends").contains(target.getUniqueId().toString())) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.Add.Already-Friends").replace("%p%", target.getName())));
			return;
		}
		
		/* ADD TO TARGET'S INCOMING LIST */
		try {
			List<String> incoming = targetConfig.getStringList("Incoming-Requests");
			incoming.add(p.getUniqueId().toString());
			targetConfig.set("Incoming-Requests", incoming);
			targetConfig.save(playerData2);
		} catch (Exception ex) {
			System.out.println(ChatColor.RED + "Failed to save data file for " + target.getName());
			return;
		}
		
		/* SEND MESSAGES */
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Add.Sent").replace("%p%", target.getName())));
		target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Add.Receive").replace("%p%", p.getName())));

		/* SET TIMER */
		int timer = plugin.getConfig().getInt("Request-Timer");
		new BukkitRunnable() {
			
			@Override
			public void run() {
				/* IS STILL AWAITING A RESPONSE */
				try {
					/* RE-LOADS THE CONFIG FOR SECURITY */
					playerConfig.load(playerData);
					targetConfig.load(playerData2);
				} catch (Exception ex) {
					System.out.println(ChatColor.RED + "Failed to load data files...");
					return;
				}
				if (targetConfig.getStringList("Incoming-Requests").contains(p.getUniqueId().toString())) {
					try {
						  // Remove UUID from incoming-requests list
						List<String> reqs = targetConfig.getStringList("Incoming-Requests");
						reqs.remove(p.getUniqueId().toString());
						targetConfig.set("Incoming-Requests", reqs);
						targetConfig.save(playerData2);
					} catch (Exception ex) {
						return;
					}
					  /* SEND MESSAGES */
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Add.Expired-Player").replace("%p%", target.getName())));
					target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Messages.Add.Expired-Target").replace("%p%", p.getName())));
				}
			}
		}.runTaskLater(plugin, 20*timer);
		
		return;
	}
	
}
