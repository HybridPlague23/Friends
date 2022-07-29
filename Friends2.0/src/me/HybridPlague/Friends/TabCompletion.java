package me.HybridPlague.Friends;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompletion implements TabCompleter {

	List<String> arguments = new ArrayList<String>();
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (arguments.isEmpty()) {
			arguments.add("Add"); arguments.add("Block");
			arguments.add("Accept"); arguments.add("BlockList");
			arguments.add("Deny"); arguments.add("Help");
			arguments.add("UnblockAll"); arguments.add("List");
			arguments.add("Remove"); arguments.add("ToggleRequests");
			arguments.add("ToggleReq"); arguments.add("ToggleFind");
			arguments.add("Unblock"); arguments.add("AcceptAll");
			arguments.add("RemoveAll"); arguments.add("DenyAll");
		}
		
		List<String> result = new ArrayList<String>();
		if (args.length == 1) {
			for (String a : arguments) {
				if (a.toLowerCase().startsWith(args[0].toLowerCase()))
					result.add(a);
			}
			return result;
		}
		
		
		return null;
	}
	
}
