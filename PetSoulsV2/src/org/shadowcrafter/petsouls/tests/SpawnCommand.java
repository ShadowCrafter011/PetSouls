package org.shadowcrafter.petsouls.tests;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shadowcrafter.petsouls.PetSouls;
import org.shadowcrafter.petsouls.pets.types.PetWolf;

public class SpawnCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		Player p = (Player) sender;
		
		((PetWolf) PetSouls.getPlugin().getConfig().get("1")).spawn(p.getLocation());
		
		p.sendMessage("spawned");
		
		return false;
	}

}
