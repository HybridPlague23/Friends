package me.HybridPlague.Friends.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.HybridPlague.Friends.Main;

public class CommandBlockList {

	public static List<Inventory> blocks = new ArrayList<Inventory>();
	public static HashMap<Player, Integer> page = new HashMap<Player, Integer>();
	
	public Inventory blockList;
	
	public YamlConfiguration playerConfig = new YamlConfiguration();
	
	private Main plugin;
	public CommandBlockList(Main plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	public void commandBlockList(final Player p, Integer pages) {
		
		File playerData = new File(plugin.getDataFolder(), "players/" + p.getUniqueId().toString() + ".yml");
		
		/* LOAD CONFIGS */
		
		try {
			playerConfig.load(playerData);
		} catch (Exception ex) {
		}
		
		/*-------------*/
		
		
		/* DOES PLAYER HAVE BLOCKS */
		if (playerConfig.getStringList("Blocks").size() == 0 || playerConfig.getStringList("Blocks") == null) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.List.No-Blocks")));
			return;
		}
		
		  // Has blocks
		
		blockList = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Blocked-List.Header")
				.replace("%page%", String.valueOf(pages))
				.replace("%count%", String.valueOf(playerConfig.getStringList("Blocks").size()))));
		
		blocks.add(blockList);
		p.openInventory(blockList);
		
		int total = playerConfig.getStringList("Blocks").size();
		
		ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        
        ItemStack hotbar = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta hotbarMeta = hotbar.getItemMeta();
		
        List<String> lore = new ArrayList<String>();
		
		
        /* PAGE 1 */
		if (pages == 1) {
			page.put(p, 1);
			
			try {
				/* FILLER */
				if (plugin.getConfig().getBoolean("GUI-Editor.Blocked-List.Filler.enabled") == true) {
					
					for (int i = 0; i < 45; i++) {
						hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Blocked-List.Filler.item").toUpperCase()));
						hotbarMeta.setDisplayName(" ");
						hotbar.setItemMeta(hotbarMeta);
						blockList.setItem(i, hotbar);
					}
					
				}
				
				/* HOTBAR */ 
				for (int i = 45; i < 54; i++) {
					hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Blocked-List.Hotbar.Filler.item").toUpperCase()));
					hotbarMeta.setDisplayName(" ");
					hotbar.setItemMeta(hotbarMeta);
					blockList.setItem(i, hotbar);
				}
				
				hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.General.Close.item").toUpperCase()));
				hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Close.name")));
				hotbar.setItemMeta(hotbarMeta);
				blockList.setItem(49, hotbar);
				
				if (total > 45) {
					hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.General.Next.item").toUpperCase()));
					hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Next.name")));
					hotbar.setItemMeta(hotbarMeta);
					blockList.setItem(53, hotbar);
					
				}
				/**********/
				
				try {
					// LOOP BLOCKED LIST
					for (int i = 0; i < 45; i++) {
						UUID id = UUID.fromString(playerConfig.getStringList("Blocks").toArray()[i].toString()); // Get UUID
						OfflinePlayer blocked = Bukkit.getOfflinePlayer(id); // Get player tag
						
						// LORES
						for (String lores : plugin.getConfig().getStringList("GUI-Editor.Blocked-List.Player-Heads.lore")) {
							lore.add(ChatColor.translateAlternateColorCodes('&', lores)
									.replace("%date%", playerConfig.getString("Blocked-Dates." + id)));
						}
						
						/* ADD ITEM TO GUI */
						meta.setOwner(blocked.getName());
						meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Blocked-List.Player-Heads.name")
								.replace("%p%", blocked.getName())));
						meta.setLore(lore);
						item.setItemMeta(meta);
						blockList.setItem(i, item);
						lore.clear();
					}
				} catch (Exception ex) {
					// do nothing -- THROWS OutOfBound Exceptions trying to loop blocked list.
				}
			} catch (Exception ex) {
				
			}
		}
		
		/* FURTHER PAGES */
		if (pages > 1) {
			page.put(p, pages++);
			try {
				int position = 0;
				/* FILLER */
				if (plugin.getConfig().getBoolean("GUI-Editor.Blocked-List.Filler.enabled") == true) {
					
					for (int i = 0; i < 54; i++) {
						hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Blocked-List.Filler.item").toUpperCase()));
						hotbarMeta.setDisplayName(" ");
						hotbar.setItemMeta(hotbarMeta);
						blockList.setItem(i, hotbar);
					}
					
				}
				
				/* HOTBAR */ 
				for (int i = 45; i < 54; i++) {
					hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Blocked-List.Hotbar.Filler.item").toUpperCase()));
					hotbarMeta.setDisplayName(" ");
					hotbar.setItemMeta(hotbarMeta);
					blockList.setItem(i, hotbar);
				}
				
				hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.General.Back.item").toUpperCase()));
				hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Back.name")));
				hotbar.setItemMeta(hotbarMeta);
				blockList.setItem(45, hotbar);
				
				hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.General.Close.item").toUpperCase()));
				hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Close.name")));
				hotbar.setItemMeta(hotbarMeta);
				blockList.setItem(48, hotbar);
				
				if (total > (page.get(p)) * 45) {
					hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.General.Next.item").toUpperCase()));
					hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Next.name")));
					hotbar.setItemMeta(hotbarMeta);
					blockList.setItem(53, hotbar);
					
				}
				
				for (int i = 0; i < 45; i++) {
					UUID id = UUID.fromString(playerConfig.getStringList("Blocks").toArray()[(position + ((page.get(p) - 1) * 45))].toString()); // Get UUID
					OfflinePlayer blocked = Bukkit.getOfflinePlayer(id); // Get player tag
					// LORES
					for (String lores : plugin.getConfig().getStringList("GUI-Editor.Blocked-List.Player-Heads.lore")) {
						lore.add(ChatColor.translateAlternateColorCodes('&', lores)
								.replace("%date%", playerConfig.getString("Blocked-Dates." + id)));
					}
					
					/* ADD ITEM TO GUI */
					meta.setOwner(blocked.getName());
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Blocked-List.Player-Heads.name")
							.replace("%p%", blocked.getName())));
					meta.setLore(lore);
					item.setItemMeta(meta);
					blockList.setItem(i, item);
					lore.clear();
					position++;
				}
			} catch (Exception ex) {
				// do nothing -- THROWS OutOfBound Exceptions trying to loop blocked list.
			}
			
		}
	}
	
}
