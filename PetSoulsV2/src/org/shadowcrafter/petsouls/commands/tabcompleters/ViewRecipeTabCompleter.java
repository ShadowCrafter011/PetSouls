package org.shadowcrafter.petsouls.commands.tabcompleters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.shadowcrafter.petsouls.items.ItemKey;
import org.shadowcrafter.petsouls.util.StringUtils;

public class ViewRecipeTabCompleter extends StringUtils implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> output = new ArrayList<>();
		
		if (sender instanceof Player == false) return asList("");
		
		if (args.length != 1) return asList("");
		
		for (ItemKey key : ItemKey.values()) {
			output.add(key.toString());
		}
		return StringUtil.copyPartialMatches(args[0], output, new ArrayList<>());
	}

}
