package me.HybridPlague.Friends.DataManager;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.HybridPlague.Friends.Main;

public class PlayerData {

	private Main plugin;
	public PlayerData(Main plugin) {
		this.plugin = plugin;
	}
	
	public void fileManager(final Player p) {
		
		File playerData = new File(plugin.getDataFolder(), "players/" + p.getUniqueId().toString() + ".yml");
		
		/* Check if the player's file exists */
		
		if (!playerData.exists()) {
			
			/* If not, create it */
			
			try {

				System.out.println(ChatColor.GREEN + "Player data file not found.. Creating one!");
				playerData.createNewFile();
			} catch (Exception ex) {
				System.out.println(ChatColor.RED + "Error..could not create player data file for " + p.getName());
				return;
				
			}
		}
		
		/* File already exists, you're good! */
	}
	
}
