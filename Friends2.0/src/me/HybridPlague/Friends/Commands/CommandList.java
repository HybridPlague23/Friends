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
import me.clip.placeholderapi.PlaceholderAPI;

public class CommandList {

	// For local data storage
	public static List<Inventory> friends = new ArrayList<Inventory>();
	public static HashMap<Player, Integer> page = new HashMap<Player, Integer>();
	
	public Inventory friendList;
	
	public YamlConfiguration playerConfig = new YamlConfiguration();
	
	private Main plugin;
	public CommandList(Main plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	public void commandList(final Player p, Integer pages) {
		
		File playerData = new File(plugin.getDataFolder(), "players/" + p.getUniqueId().toString() + ".yml");
		
		/* LOAD CONFIG */
		
		try {
			playerConfig.load(playerData);
		} catch (Exception ex) {
		}
		
		/*-------------*/
		
		
		/* DOES PLAYER HAVE FRIENDS */
		if (playerConfig.getStringList("Friends").size() == 0 || playerConfig.getStringList("Friends") == null) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + plugin.getConfig().getString("Error-Messages.List.No-Friends")));
			return;
		}
		
		friendList = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-List.Header")
				.replace("%page%", String.valueOf(pages))
				.replace("%count%", String.valueOf(playerConfig.getStringList("Friends").size()))));
		
		friends.add(friendList);
		p.openInventory(friendList);
		
		int total = playerConfig.getStringList("Friends").size();
		
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
				if (plugin.getConfig().getBoolean("GUI-Editor.Friend-List.Filler.enabled") == true) {
					
					for (int i = 0; i < 45; i++) {
						hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Friend-List.Filler.item").toUpperCase()));
						hotbarMeta.setDisplayName(" ");
						hotbar.setItemMeta(hotbarMeta);
						friendList.setItem(i, hotbar);
					}
					
				}
				
				/* HOTBAR */ 
				for (int i = 45; i < 54; i++) {
					hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Friend-List.Hotbar.Filler.item").toUpperCase()));
					hotbarMeta.setDisplayName(" ");
					hotbar.setItemMeta(hotbarMeta);
					friendList.setItem(i, hotbar);
				}
				
				hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.General.Close.item").toUpperCase()));
				hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Close.name")));
				hotbar.setItemMeta(hotbarMeta);
				friendList.setItem(48, hotbar);
				
				if (total > 45) {
					hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.General.Next.item").toUpperCase()));
					hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Next.name")));
					hotbar.setItemMeta(hotbarMeta);
					friendList.setItem(53, hotbar);
					
				}
				try {
					for (int i = 0; i < 45; i++) {
						UUID id = UUID.fromString(playerConfig.getStringList("Friends").toArray()[i].toString()); // Get UUID
						OfflinePlayer friend = Bukkit.getOfflinePlayer(id); // Get player tag
						
						/* GET STATUS */
						String online = null;
						// is friend online
						if (friend.isOnline()) {
							// does server have SuperVanish enabled
							if (plugin.getServer().getPluginManager().isPluginEnabled("SuperVanish")) {
								String isVanished = "%supervanish_isvanished%";
								isVanished = PlaceholderAPI.setPlaceholders(friend, isVanished);
								// friend is not vanished
								if (isVanished.equalsIgnoreCase("No")) {
									online = "&aOnline";
								}
								// friend is vanished
								if (isVanished.equalsIgnoreCase("Yes")) {
									online = "&4Offline";
								}
							}
							// server does not have SuperVanish
							else if (!plugin.getServer().getPluginManager().isPluginEnabled("SuperVanish")) {
								online = "&aOnline";
							}
						}
						if (!friend.isOnline()) {
							online = "&4Offline";
						}
						
						/* SET LORES */
						for (String lores : plugin.getConfig().getStringList("GUI-Editor.Friend-List.Player-Heads.lore")) {
							lore.add(ChatColor.translateAlternateColorCodes('&', lores)
									.replace("%date%", playerConfig.getString("Added-Dates." + id))
									.replace("%status%", ChatColor.translateAlternateColorCodes('&', online)));
						}
						
						/* ADD ITEM TO GUI */
						meta.setOwner(friend.getName());
						meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-List.Player-Heads.name")
								.replace("%p%", friend.getName())));
						meta.setLore(lore);
						item.setItemMeta(meta);
						friendList.setItem(i, item);
						lore.clear();
						
						String find = null;
						if (playerConfig.getString("Toggle.Find") == null || playerConfig.getString("Toggle.Find").equalsIgnoreCase("On")) 
							find = plugin.getConfig().getString("GUI-Editor.Friend-List.Hotbar.ToggleFind.on-type");
						if (playerConfig.getString("Toggle.Find").equalsIgnoreCase("Off")) 
							find = plugin.getConfig().getString("GUI-Editor.Friend-List.Hotbar.ToggleFind.off-type");
						
						hotbar.setType(Material.matchMaterial(find.toUpperCase()));
						hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-List.Hotbar.ToggleFind.name")));
						for (String lores : plugin.getConfig().getStringList("GUI-Editor.Friend-List.Hotbar.ToggleFind.lore")) {
							lore.add(ChatColor.translateAlternateColorCodes('&', lores));
						}
						hotbarMeta.setLore(lore);
						hotbar.setItemMeta(hotbarMeta);
						friendList.setItem(49, hotbar);
						lore.clear();
						
						// Get toggle status
						String req = null;
						if (playerConfig.getString("Toggle.Requests") == null || playerConfig.getString("Toggle.Requests").equalsIgnoreCase("On")) 
							req = plugin.getConfig().getString("GUI-Editor.Friend-List.Hotbar.ToggleReq.on-type");
						if (playerConfig.getString("Toggle.Requests").equalsIgnoreCase("Off")) 
							req = plugin.getConfig().getString("GUI-Editor.Friend-List.Hotbar.ToggleReq.off-type");
						
						hotbar.setType(Material.matchMaterial(req.toUpperCase()));
						hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-List.Hotbar.ToggleReq.name")));
						for (String lores : plugin.getConfig().getStringList("GUI-Editor.Friend-List.Hotbar.ToggleReq.lore")) {
							lore.add(ChatColor.translateAlternateColorCodes('&', lores));
						}
						hotbarMeta.setLore(lore);
						hotbar.setItemMeta(hotbarMeta);
						friendList.setItem(50, hotbar);
						lore.clear();
						
					}
				} catch (Exception ex) {
					// do nothing -- THROWS OutOfBound Exceptions trying to loop friends list.
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}
		
		/* FURTHER PAGES */
		if (pages > 1) {
			page.put(p, pages++);
			try {
				int position = 0;
				/* FILLER */
				if (plugin.getConfig().getBoolean("GUI-Editor.Friend-List.Filler.enabled") == true) {
					
					for (int i = 0; i < 54; i++) {
						hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Friend-List.Filler.item").toUpperCase()));
						hotbarMeta.setDisplayName(" ");
						hotbar.setItemMeta(hotbarMeta);
						friendList.setItem(i, hotbar);
					}
					
				}
				
				/* HOTBAR */ 
				for (int i = 45; i < 54; i++) {
					hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Friend-List.Hotbar.Filler.item").toUpperCase()));
					hotbarMeta.setDisplayName(" ");
					hotbar.setItemMeta(hotbarMeta);
					friendList.setItem(i, hotbar);
				}
				
				hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.General.Back.item").toUpperCase()));
				hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Back.name")));
				hotbar.setItemMeta(hotbarMeta);
				friendList.setItem(45, hotbar);
				
				hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.General.Close.item").toUpperCase()));
				hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Close.name")));
				hotbar.setItemMeta(hotbarMeta);
				friendList.setItem(48, hotbar);
				
				if (total > (page.get(p)) * 45) {
					hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.General.Next.item").toUpperCase()));
					hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Next.name")));
					hotbar.setItemMeta(hotbarMeta);
					friendList.setItem(53, hotbar);
					
				}
				
				for (int i = 0; i < 45; i++) {
					UUID id = UUID.fromString(playerConfig.getStringList("Friends").toArray()[(position + ((page.get(p) - 1) * 45))].toString()); // Get UUID
					OfflinePlayer friend = Bukkit.getOfflinePlayer(id); // Get player tag
					/* GET STATUS */
					String online = null;
					// is friend online
					if (friend.isOnline()) {
						// does server have SuperVanish enabled
						if (plugin.getServer().getPluginManager().isPluginEnabled("SuperVanish")) {
							String isVanished = "%supervanish_isvanished%";
							isVanished = PlaceholderAPI.setPlaceholders(friend, isVanished);
							// friend is not vanished
							if (isVanished.equalsIgnoreCase("No")) {
								online = "&aOnline";
							}
							// friend is vanished
							if (isVanished.equalsIgnoreCase("Yes")) {
								online = "&4Offline";
							}
						}
						// server does not have SuperVanish
						else if (!plugin.getServer().getPluginManager().isPluginEnabled("SuperVanish")) {
							online = "&aOnline";
						}
					}
					if (!friend.isOnline()) {
						online = "&4Offline";
					}
					
					/* SET LORES */
					for (String lores : plugin.getConfig().getStringList("GUI-Editor.Friend-List.Player-Heads.lore")) {
						lore.add(ChatColor.translateAlternateColorCodes('&', lores)
								.replace("%date%", playerConfig.getString("Added-Dates." + id))
								.replace("%status%", ChatColor.translateAlternateColorCodes('&', online)));
					}
					
					/* ADD ITEM TO GUI */
					meta.setOwner(friend.getName());
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-List.Player-Heads.name")
							.replace("%p%", friend.getName())));
					meta.setLore(lore);
					item.setItemMeta(meta);
					friendList.setItem(i, item);
					lore.clear();
					
					// Get toggle status
					String status = null;
					if (playerConfig.getString("Toggled") == null || playerConfig.getString("Toggled").equalsIgnoreCase("On")) 
						status = plugin.getConfig().getString("GUI-Editor.Friend-List.Hotbar.Toggle.on-type");
					if (playerConfig.getString("Toggled").equalsIgnoreCase("Off")) 
						status = plugin.getConfig().getString("GUI-Editor.Friend-List.Hotbar.Toggle.off-type");
					
					hotbar.setType(Material.matchMaterial(status.toUpperCase()));
					hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-List.Hotbar.Toggle.name")));
					for (String lores : plugin.getConfig().getStringList("GUI-Editor.Friend-List.Hotbar.Toggle.lore")) {
						lore.add(ChatColor.translateAlternateColorCodes('&', lores));
					}
					hotbarMeta.setLore(lore);
					hotbar.setItemMeta(hotbarMeta);
					friendList.setItem(50, hotbar);
					lore.clear();
					position++;
				}
				
			} catch (Exception ex) {
				
			}
		}
		
		
	}
	
}
