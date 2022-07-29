package me.HybridPlague.Friends;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.HybridPlague.Friends.Commands.CommandBlockList;
import me.HybridPlague.Friends.Commands.CommandList;
import me.HybridPlague.Friends.OptionInventories.BlockListOptions;
import me.HybridPlague.Friends.OptionInventories.FriendListOptions;

@SuppressWarnings("deprecation")
public class FriendEvents implements Listener {

	public YamlConfiguration playerConfig = new YamlConfiguration();
	
	public static HashMap<Player, OfflinePlayer> viewing = new HashMap<Player, OfflinePlayer>();
	public static List<Player> pay = new ArrayList<Player>();
	public static List<Player> mail = new ArrayList<Player>();
	
	private Main plugin;
	public FriendEvents(Main plugin) {
		this.plugin = plugin;
	}
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = (Player) e.getPlayer();
		
		if (pay.contains(p)) {
			String amount = e.getMessage();
			e.setCancelled(true);
			
			if (amount.equalsIgnoreCase("cancel")
					|| amount.equalsIgnoreCase("stop")) {
				pay.remove(p);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + "&aTransaction has been cancel."));
				return;
			}
			
			if (!isNum(amount)) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + "&cSyntax error: &eInvalid integer. If you would like to cancel this transaction, type &fcancel &eor &fstop&e."));
				return;
			}
			this.sendMoney(p, viewing.get(p), amount);
			pay.remove(p);
			return;
			
		}
		
		if (mail.contains(p)) {
			e.setCancelled(true);
			
			String msg = e.getMessage();
			
			if (msg.equalsIgnoreCase("cancel")
					|| msg.equalsIgnoreCase("stop")) {
				mail.remove(p);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix + "&aStopping mail letter."));
				return;
			}
			
			this.sendMail(p, viewing.get(p), msg);
			mail.remove(p);
			return;
		}
		return;
		
	}
	
	public void sendMoney(final Player p, final OfflinePlayer target, String amount) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				p.performCommand("offlinepay " + viewing.get(p).getName() + " " + amount);
			}
		}.runTaskLater(plugin, 1);
		return;
	}
	
	public void sendMail(final Player p, final OfflinePlayer target, String msg) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				p.performCommand("mail send " + viewing.get(p).getName() + " " + msg);
			}
		}.runTaskLater(plugin, 1);
		return;
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = (Player) e.getPlayer();
		plugin.pData.fileManager(p);
		
		if (mail.contains(p)) {
			mail.remove(p);
		}
		if (pay.contains(p)) {
			pay.remove(p);
		}
		
		File playerData = new File(plugin.getDataFolder(), "players/" + p.getUniqueId().toString() + ".yml");
		/* LOAD CONFIG */
		
		try {
			playerConfig.load(playerData);
		} catch (Exception ex) {
		}
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = (Player) e.getPlayer();
		plugin.pData.fileManager(p);
		
		File playerData = new File(plugin.getDataFolder(), "players/" + p.getUniqueId().toString() + ".yml");
		
		/* LOAD CONFIG */
		
		try {
			playerConfig.load(playerData);
		} catch (Exception ex) {
		}
		
		/*-------------*/
		
		if (playerConfig.getString("Toggle.Find") == null) {
			try {
				playerConfig.set("Toggle.Find", "Off");
				playerConfig.save(playerData);
			} catch (Exception ex) {
			}
			
		}
		
		if (playerConfig.getString("Toggle.Requests") == null) {
			try {
				playerConfig.set("Toggle.Requests", "On");
				playerConfig.save(playerData);
			} catch (Exception ex) {
			}
		}
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		Player p = (Player) e.getWhoClicked();
		
		try {
			
			if (FriendListOptions.friendOpts.contains(e.getInventory())) {
				if (e.getCurrentItem() == null) return;
				if (e.getCurrentItem().getItemMeta() == null) return;
				e.setCancelled(true);
				
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Close.name")))) {
					p.closeInventory();
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Back.name")))) {
					plugin.list.commandList(p, CommandList.page.get(p));
				}
				
				// REMOVE
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-Options.Remove.name")))) {
					p.performCommand("f remove " + viewing.get(p).getName());
					plugin.list.commandList(p, CommandList.page.get(p));
				}
				// BLOCK
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-Options.Block.name")))) {
					p.performCommand("f block " + viewing.get(p).getName());
					plugin.list.commandList(p, CommandList.page.get(p));
				}
				// PAY
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-Options.Pay.name")))) {
					for (String msgs : plugin.getConfig().getStringList("GUI-Editor.Friend-Options.Pay.chat-message")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', msgs).replace("%p%", viewing.get(p).getName()));
					}
					p.closeInventory();
					pay.add(p);
				}
				// PLAYERINFO
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-Options.PlayerInfo.name")))) {
					p.performCommand("playerinfo " + viewing.get(p).getName());
				}
				// SEEN
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-Options.Seen.name")))) {
					p.closeInventory();
					p.performCommand("seen " + viewing.get(p).getName());
				}
				// MAIL
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-Options.Mail.name")))) {
					for (String msgs : plugin.getConfig().getStringList("GUI-Editor.Friend-Options.Mail.chat-message")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', msgs).replace("%p%", viewing.get(p).getName()));
					}
					p.closeInventory();
					mail.add(p);
				}
				
			}
			
			if (BlockListOptions.blockOpts.contains(e.getInventory())) {
				if (e.getCurrentItem() == null) return;
				if (e.getCurrentItem().getItemMeta() == null) return;
				e.setCancelled(true);
				
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Close.name")))) {
					p.closeInventory();
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Back.name")))) {
					plugin.blocklist.commandBlockList(p, CommandBlockList.page.get(p));
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Blocked-Options.Unblock.name")))) {
					p.performCommand("f unblock " + viewing.get(p).getName());
					plugin.blocklist.commandBlockList(p, CommandBlockList.page.get(p));
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Blocked-Options.Unblock-Add.name")))) {
					p.performCommand("f unblock " + viewing.get(p).getName());
					p.performCommand("f add " + viewing.get(p).getName());
					plugin.blocklist.commandBlockList(p, CommandBlockList.page.get(p));
				}
			}
			
			if (CommandBlockList.blocks.contains(e.getInventory())) {
				if (e.getCurrentItem() == null) return;
				if (e.getCurrentItem().getItemMeta() == null) return;
				e.setCancelled(true);
				
				if (e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
					OfflinePlayer target = Bukkit.getOfflinePlayer(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					viewing.put(p, target);
					plugin.blockOpts.blockListOpts(p, target);
				}
				
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Close.name")))) {
					p.closeInventory();
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Back.name")))) {
					CommandBlockList.page.put(p, (CommandBlockList.page.get(p) - 1));
					plugin.blocklist.commandBlockList(p, (CommandBlockList.page.get(p)));
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Next.name")))) {
					CommandBlockList.page.put(p, (CommandBlockList.page.get(p) + 1));
					plugin.blocklist.commandBlockList(p, (CommandBlockList.page.get(p)));
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Blocked-List.Hotbar.Toggle.name")))) {
					p.performCommand("f toggle");
					plugin.blocklist.commandBlockList(p, (CommandBlockList.page.get(p)));
				}
			}
			
			if (CommandList.friends.contains(e.getInventory())) {
				if (e.getCurrentItem() == null) return;
				if (e.getCurrentItem().getItemMeta() == null) return;
				e.setCancelled(true);
				
				if (e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
					OfflinePlayer target = Bukkit.getOfflinePlayer(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					viewing.put(p, target);
					plugin.friendOpts.friendListOpts(p, target);
				}
				
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Close.name")))) {
					p.closeInventory();
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Back.name")))) {
					CommandList.page.put(p, (CommandList.page.get(p) - 1));
					plugin.list.commandList(p, (CommandList.page.get(p)));
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.General.Next.name")))) {
					CommandList.page.put(p, (CommandList.page.get(p) + 1));
					plugin.list.commandList(p, (CommandList.page.get(p)));
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-List.Hotbar.ToggleFind.name")))) {
					p.performCommand("f togglefind");
					plugin.list.commandList(p, (CommandList.page.get(p)));
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI-Editor.Friend-List.Hotbar.ToggleReq.name")))) {
					p.performCommand("f togglerequests");
					plugin.list.commandList(p, (CommandList.page.get(p)));
				}
			}
			
		} catch (Exception ex) {
			
		}
		
		
	}
	public boolean isNum(String num) {
		try {
			Integer.parseInt(num);
		} catch (Exception e) {
			return false;
		}
		return true;
		
	}
}
