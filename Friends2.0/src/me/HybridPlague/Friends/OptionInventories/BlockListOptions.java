package me.HybridPlague.Friends.OptionInventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.HybridPlague.Friends.Main;

public class BlockListOptions {

	
	public static List<Inventory> blockOpts = new ArrayList<Inventory>();
	
	private Main plugin;
	public BlockListOptions(Main plugin) {
		this.plugin = plugin;
		
	}
	
	public Inventory blockOptions;
	
	@SuppressWarnings("deprecation")
	public void blockListOpts(final Player p, final OfflinePlayer target) {
		
		blockOptions = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Blocked-Options.Header")
				.replace("%p%", target.getName())));
		blockOpts.add(blockOptions);
		p.openInventory(blockOptions);
		
		ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        
        ItemStack hotbar = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta hotbarMeta = hotbar.getItemMeta();
        
		List<String> lore = new ArrayList<String>();
		
		if (plugin.getConfig().getBoolean("GUI-Editor.Blocked-Options.Filler.enabled") == true) {
			for (int i = 0; i < 45; i++) {
				hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Blocked-Options.Filler.item").toUpperCase()));
				hotbarMeta.setDisplayName(" ");
				hotbar.setItemMeta(hotbarMeta);
				blockOptions.setItem(i, hotbar);
			}
		}

		for (int i = 45; i < 54; i++) {
			hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Blocked-Options.Hotbar.Filler.item").toUpperCase()));
			hotbarMeta.setDisplayName(" ");
			hotbar.setItemMeta(hotbarMeta);
			blockOptions.setItem(i, hotbar);
		}
		
		hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.General.Back.item").toUpperCase()));
		hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Back.name")));
		hotbar.setItemMeta(hotbarMeta);
		blockOptions.setItem(45, hotbar);
		
		hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.General.Close.item").toUpperCase()));
		hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Close.name")));
		hotbar.setItemMeta(hotbarMeta);
		blockOptions.setItem(49, hotbar);
		
		// MAIN HEAD
		headMeta.setOwner(target.getName());
		headMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7" + target.getName()));
		head.setItemMeta(headMeta);
		blockOptions.setItem(13, head);
		
		// UNBLOCK
		item.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Blocked-Options.Unblock.item").toUpperCase()));
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Blocked-Options.Unblock.name").replace("%p%", target.getName())));
		for (String lores : plugin.getConfig().getStringList("GUI-Editor.Blocked-Options.Unblock.lore")) {
			lore.add(ChatColor.translateAlternateColorCodes('&', lores)
					.replace("%p%", target.getName()));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		blockOptions.setItem(30, item);
		lore.clear();
		
		// UNBLOCK-ADD
		item.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Blocked-Options.Unblock-Add.item").toUpperCase()));
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Blocked-Options.Unblock-Add.name").replace("%p%", target.getName())));
		for (String lores : plugin.getConfig().getStringList("GUI-Editor.Blocked-Options.Unblock-Add.lore")) {
			lore.add(ChatColor.translateAlternateColorCodes('&', lores)
					.replace("%p%", target.getName()));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		blockOptions.setItem(32, item);
		lore.clear();
		
		
	}
	
}
