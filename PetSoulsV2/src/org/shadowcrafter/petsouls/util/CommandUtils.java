package org.shadowcrafter.petsouls.util;

import org.bukkit.command.CommandSender;

public class CommandUtils {
	
	public static boolean endCommand(CommandSender sender, String message) {
		sender.sendMessage(message);
		return true;
	}

}
