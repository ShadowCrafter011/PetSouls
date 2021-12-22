package org.shadowcrafter.petsouls.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.shadowcrafter.petsouls.PetSouls;
import org.shadowcrafter.petsouls.util.CommandUtils;

public class DisablePlugin extends CommandUtils implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player == false) return endCommand(sender, "§cSorry, but only players may use this command");
		Player p = (Player) sender;
		
		if (!p.getUniqueId().toString().equals("769b3e57-285a-496b-bbb2-7daf03a9548f")) endCommand(sender, "§cSorry, but you don't have enough permissions to use this command");
		
		if (args.length != 0) endCommand(sender, "§cPlease use §5/disablepetsouls");
		
		FileConfiguration config = PetSouls.getPlugin().getConfig();
		
		config.set("enabled", !config.getBoolean("enabled"));
		PetSouls.getPlugin().saveConfig();
		
		Bukkit.getServer().reload();
		return endCommand(sender, "§aPet souls is now " + (config.getBoolean("enabled") ? "enabled" : "disabled"));
	}

}
