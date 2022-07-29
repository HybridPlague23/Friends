package me.HybridPlague.Friends;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.HybridPlague.Friends.Commands.CommandAccept;
import me.HybridPlague.Friends.Commands.CommandAcceptAll;
import me.HybridPlague.Friends.Commands.CommandAdd;
import me.HybridPlague.Friends.Commands.CommandBlock;
import me.HybridPlague.Friends.Commands.CommandBlockList;
import me.HybridPlague.Friends.Commands.CommandDeny;
import me.HybridPlague.Friends.Commands.CommandDenyAll;
import me.HybridPlague.Friends.Commands.CommandFind;
import me.HybridPlague.Friends.Commands.CommandHelp;
import me.HybridPlague.Friends.Commands.CommandList;
import me.HybridPlague.Friends.Commands.CommandRemove;
import me.HybridPlague.Friends.Commands.CommandRemoveAll;
import me.HybridPlague.Friends.Commands.CommandToggleFind;
import me.HybridPlague.Friends.Commands.CommandToggleRequests;
import me.HybridPlague.Friends.Commands.CommandUnblock;
import me.HybridPlague.Friends.Commands.CommandUnblockAll;
import me.HybridPlague.Friends.DataManager.PlayerData;
import me.HybridPlague.Friends.OptionInventories.BlockListOptions;
import me.HybridPlague.Friends.OptionInventories.FriendListOptions;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	public String prefix = this.getConfig().getString("Prefix");
	
	/* GET CLASSES */
	public PlayerData pData;
	
	public CommandAccept accept;
	public CommandAcceptAll acceptall;
	public CommandAdd add;
	
	public CommandBlock block;
	public CommandBlockList blocklist;
	
	public CommandDeny deny;
	public CommandDenyAll denyall;
	
	public CommandHelp help;
	
	public CommandList list;
	
	public CommandFind find;
	
	public CommandRemove remove;
	public CommandRemoveAll removeall;
	
	public CommandToggleRequests togglereqs;
	public CommandToggleFind togglefind;
	
	public CommandUnblock unblock;
	public CommandUnblockAll unblockall;
	
	public BlockListOptions blockOpts;
	public FriendListOptions friendOpts;
	
	public Economy eco;
	@Override
	public void onEnable() {
		
		if (!setupEconomy()) {
			System.out.println(ChatColor.RED + "You must have Vault" + " and an economy plugin installed.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(new FriendEvents(this), this);
		
		/* CREATE DATA FOLDER */
		if (!this.getDataFolder().exists()) {
			this.getDataFolder().mkdir();
		}
		
		File players = new File(this.getDataFolder(), "players");
		if (!players.exists()) {
			players.mkdirs();
		}
		/**********************/
		
		/* GET CLASSES */
		this.pData = new PlayerData(this);
		
		this.accept = new CommandAccept(this);
		this.acceptall = new CommandAcceptAll(this);
		this.add = new CommandAdd(this);
		
		this.block = new CommandBlock(this);
		this.blocklist = new CommandBlockList(this);
		
		this.deny = new CommandDeny(this);
		this.denyall = new CommandDenyAll(this);
		
		this.help = new CommandHelp(this);
		
		this.list = new CommandList(this);
		
		this.find = new CommandFind(this);
		
		this.remove = new CommandRemove(this);
		this.removeall = new CommandRemoveAll(this);
		
		this.togglereqs = new CommandToggleRequests(this);
		this.togglefind = new CommandToggleFind(this);
		
		this.unblock = new CommandUnblock(this);
		this.unblockall = new CommandUnblockAll(this);
		
		this.blockOpts = new BlockListOptions(this);
		this.friendOpts = new FriendListOptions(this);
		
		this.getCommand("friend").setTabCompleter(new TabCompletion());
		
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("friends") || label.equalsIgnoreCase("friend") || label.equalsIgnoreCase("f")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command is only executable by a player.");
				return true;
			}
			Player p = (Player) sender;
			if (!p.hasPermission("friends.basic")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l(WIP) &fRevamp in progress. Return later"));
				return true;
			}
			if (args.length == 0) {
				help.commandHelp(p);
				return true;
			}
			
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("accept")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + this.getConfig().getString("Error-Messages.General.No-Arguments").replace("%command%", "accept")));
					return true;
				}
				if (args[0].equalsIgnoreCase("acceptall")) {
					acceptall.commandAcceptAll(p);
					return true;
				}
				if (args[0].equalsIgnoreCase("add")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + this.getConfig().getString("Error-Messages.General.No-Arguments").replace("%command%", "add")));
					return true;
				}
				if (args[0].equalsIgnoreCase("block")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + this.getConfig().getString("Error-Messages.General.No-Arguments").replace("%command%", "block")));
					return true;
				}
				if (args[0].equalsIgnoreCase("blocklist") || args[0].equalsIgnoreCase("blist")) {
					blocklist.commandBlockList(p, 1);
					return true;
				}
				if (args[0].equalsIgnoreCase("deny")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + this.getConfig().getString("Error-Messages.General.No-Arguments").replace("%command%", "deny")));
					return true;
				}
				if (args[0].equalsIgnoreCase("denyall")) {
					denyall.commandDenyAll(p);
					return true;
				}
				if (args[0].equalsIgnoreCase("list")) {
					list.commandList(p, 1);
					return true;
				}
				if (args[0].equalsIgnoreCase("find")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + this.getConfig().getString("Error-Messages.General.No-Arguments").replace("%command%", "find")));
					return true;
				}
				if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
					if (!p.hasPermission("friends.reload")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.RED + "Insufficient permission."));
						return true;
					}
					this.reloadConfig();
					prefix = this.getConfig().getString("Prefix");
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aConfig reloaded successfully!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("remove")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + this.getConfig().getString("Error-Messages.General.No-Arguments").replace("%command%", "remove")));
					return true;
				}
				if (args[0].equalsIgnoreCase("removeall")) {
					removeall.commandRemoveAll(p);
					return true;
				}
				if (args[0].equalsIgnoreCase("togglerequests")
						|| args[0].equalsIgnoreCase("togglereq")) {
					togglereqs.commandToggle(p);
					return true;
				}
				if (args[0].equalsIgnoreCase("togglefind")) {
					togglefind.commandToggleFind(p);
					return true;
				}
				if (args[0].equalsIgnoreCase("unblock")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + this.getConfig().getString("Error-Messages.General.No-Arguments").replace("%command%", "unblock")));
					return true;
				}
				if (args[0].equalsIgnoreCase("unblockall")) {
					unblockall.commandUnblockAll(p);
					return true;
				}
				help.commandHelp(p);
				return true;
			}
			if (args.length > 1) {
				if (args[0].equalsIgnoreCase("accept")) {
					OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
					accept.commandAccept(p, target);
					return true;
				}
				if (args[0].equalsIgnoreCase("acceptall")) {
					acceptall.commandAcceptAll(p);
					return true;
				}
				if (args[0].equalsIgnoreCase("add")) {
					/* IS THIS PLAYER ONLINE */
					if (!Bukkit.getOfflinePlayer(args[1]).isOnline()) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + this.getConfig().getString("Error-Messages.General.Unknown-Player")));
						return true;
					}
					Player target = Bukkit.getPlayer(args[1]);
					add.commandAdd(p, target);
					return true;
				}
				if (args[0].equalsIgnoreCase("block")) {
					/* DOES THIS USER EXIST */
					if (!Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + this.getConfig().getString("Error-Messages.General.Unknown-Player")));
						return true;
					}
					if (Bukkit.getOfflinePlayer(args[1]).isOnline() || Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
						block.commandBlock(p, target);
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("blocklist") || args[0].equalsIgnoreCase("blist")) {
					blocklist.commandBlockList(p, 1);
					return true;
				}
				if (args[0].equalsIgnoreCase("deny")) {
					if (Bukkit.getOfflinePlayer(args[1]).isOnline() || Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
						deny.commandDeny(p, target);
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("denyall")) {
					denyall.commandDenyAll(p);
					return true;
				}
				if (args[0].equalsIgnoreCase("list")) {
					list.commandList(p, 1);
					return true;
				}
				if (args[0].equalsIgnoreCase("find")) {
					if (Bukkit.getOfflinePlayer(args[1]).isOnline()) {
						Player target = Bukkit.getPlayer(args[1]);
						find.commandFind(p, target);
						return true;
					}
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + this.getConfig().getString("Error-Messages.General.Unknown-Player")));
					return true;
				}
				if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
					if (!p.hasPermission("friends.reload")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.RED + "Insufficient permission."));
						return true;
					}
					this.reloadConfig();
					prefix = this.getConfig().getString("Prefix");
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aConfig reloaded successfully!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("remove")) {
					if (Bukkit.getOfflinePlayer(args[1]).isOnline() || Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
						remove.commandRemove(p, target);
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("removeall")) {
					removeall.commandRemoveAll(p);
					return true;
				}
				if (args[0].equalsIgnoreCase("togglerequests")
						|| args[0].equalsIgnoreCase("togglereq")) {
					togglereqs.commandToggle(p);
					return true;
				}
				if (args[0].equalsIgnoreCase("togglefind")) {
					togglefind.commandToggleFind(p);
					return true;
				}
				if (args[0].equalsIgnoreCase("unblock")) {
					if (Bukkit.getOfflinePlayer(args[1]).isOnline() || Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
						unblock.commandUnblock(p, target);
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("unblockall")) {
					unblockall.commandUnblockAll(p);
					return true;
				}
				help.commandHelp(p);
				return true;
			}
			
		}
		return false;
	}
	
	public boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economy = getServer().
				getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economy != null)
			eco = economy.getProvider();
		return (eco != null);
	}
}
