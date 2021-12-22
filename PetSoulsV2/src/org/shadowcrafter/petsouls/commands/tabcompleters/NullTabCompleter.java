package org.shadowcrafter.petsouls.commands.tabcompleters;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.shadowcrafter.petsouls.util.StringUtils;

public class NullTabCompleter extends StringUtils implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return args.length > 0 ? asList("") : null;
	}

}
