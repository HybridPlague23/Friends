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

public class FriendListOptions {

	public static List<Inventory> friendOpts = new ArrayList<Inventory>();
	
	private Main plugin;
	public FriendListOptions(Main plugin) {
		this.plugin = plugin;
	}

	
	public Inventory friendOptions;
	
	@SuppressWarnings("deprecation")
	public void friendListOpts(final Player p, final OfflinePlayer target) {
		
		friendOptions = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-Options.Header")
				.replace("%p%", target.getName())));
		friendOpts.add(friendOptions);
		p.openInventory(friendOptions);
		
		ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        
        ItemStack hotbar = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta hotbarMeta = hotbar.getItemMeta();
        
		List<String> lore = new ArrayList<String>();
		
		double playerBalance = plugin.eco.getBalance(target);
		if (plugin.getConfig().getBoolean("GUI-Editor.Friend-Options.Filler.enabled") == true) {
			for (int i = 0; i < 45; i++) {
				hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Friend-Options.Filler.item").toUpperCase()));
				hotbarMeta.setDisplayName(" ");
				hotbar.setItemMeta(hotbarMeta);
				friendOptions.setItem(i, hotbar);
			}
		}

		for (int i = 45; i < 54; i++) {
			hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Friend-Options.Hotbar.Filler.item").toUpperCase()));
			hotbarMeta.setDisplayName(" ");
			hotbar.setItemMeta(hotbarMeta);
			friendOptions.setItem(i, hotbar);
		}
		
		hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.General.Back.item").toUpperCase()));
		hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Back.name")));
		hotbar.setItemMeta(hotbarMeta);
		friendOptions.setItem(45, hotbar);
		
		hotbar.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.General.Close.item").toUpperCase()));
		hotbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Close.name")));
		hotbar.setItemMeta(hotbarMeta);
		friendOptions.setItem(49, hotbar);
		
		// MAIN HEAD
		headMeta.setOwner(target.getName());
		headMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7" + target.getName()));
		head.setItemMeta(headMeta);
		friendOptions.setItem(13, head);
		
		int i = 28;
		
		// REMOVE
		item.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Friend-Options.Remove.item").toUpperCase()));
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-Options.Remove.name")));
		for (String lores : plugin.getConfig().getStringList("GUI-Editor.Friend-Options.Remove.lore")) {
			lore.add(ChatColor.translateAlternateColorCodes('&', lores)
					.replace("%p%", target.getName()));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		friendOptions.setItem(i, item);
		i++;
		lore.clear();
		
		// BLOCK
		item.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Friend-Options.Block.item").toUpperCase()));
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-Options.Block.name")));
		for (String lores : plugin.getConfig().getStringList("GUI-Editor.Friend-Options.Block.lore")) {
			lore.add(ChatColor.translateAlternateColorCodes('&', lores)
					.replace("%p%", target.getName()));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		friendOptions.setItem(i, item);
		i++;
		lore.clear();
		
		// BALANCE
		item.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Friend-Options.Balance.item").toUpperCase()));
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-Options.Balance.name")));
		for (String lores : plugin.getConfig().getStringList("GUI-Editor.Friend-Options.Balance.lore")) {
			lore.add(ChatColor.translateAlternateColorCodes('&', lores)
					.replace("%balance%", String.valueOf(plugin.eco.format(playerBalance))));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		friendOptions.setItem(i, item);
		i++;
		lore.clear();
		
		// PAY
		item.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Friend-Options.Pay.item").toUpperCase()));
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-Options.Pay.name")));
		for (String lores : plugin.getConfig().getStringList("GUI-Editor.Friend-Options.Pay.lore")) {
			lore.add(ChatColor.translateAlternateColorCodes('&', lores)
					.replace("%p%", target.getName()));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		friendOptions.setItem(i, item);
		i++;
		lore.clear();
		
		// PLAYERINFO
		item.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Friend-Options.PlayerInfo.item").toUpperCase()));
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-Options.PlayerInfo.name")));
		for (String lores : plugin.getConfig().getStringList("GUI-Editor.Friend-Options.PlayerInfo.lore")) {
			lore.add(ChatColor.translateAlternateColorCodes('&', lores)
					.replace("%p%", target.getName()));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		friendOptions.setItem(i, item);
		i++;
		lore.clear();
		
		// SEEN
		item.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Friend-Options.Seen.item").toUpperCase()));
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-Options.Seen.name")));
		for (String lores : plugin.getConfig().getStringList("GUI-Editor.Friend-Options.Seen.lore")) {
			lore.add(ChatColor.translateAlternateColorCodes('&', lores)
					.replace("%p%", target.getName()));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		friendOptions.setItem(i, item);
		i++;
		lore.clear();
		
		// MAIL
		item.setType(Material.matchMaterial(plugin.getConfig().getString("GUI-Editor.Friend-Options.Mail.item").toUpperCase()));
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-Options.Mail.name")));
		for (String lores : plugin.getConfig().getStringList("GUI-Editor.Friend-Options.Mail.lore")) {
			lore.add(ChatColor.translateAlternateColorCodes('&', lores)
					.replace("%p%", target.getName()));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		friendOptions.setItem(i, item);
		lore.clear();
		
	}
	
}
