package org.shadowcrafter.petsouls.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shadowcrafter.petsouls.pets.PetInterface;
import org.shadowcrafter.petsouls.util.CommandUtils;
import org.shadowcrafter.petsouls.util.Players;
import org.shadowcrafter.petsouls.util.TemporaryData;

public class SpawnAllCommand extends CommandUtils implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player == false) return endCommand(sender, "§cSorry, but only players may use this command");
		Player p = (Player) sender;
		
		if (args.length != 0) return endCommand(sender, "§cPlease use §5/spawnallpets");
		
		for (PetInterface pet : TemporaryData.get().getPets(p)) {
			if (!pet.isSpawned()) pet.spawn(p.getLocation(), false, Players.list().getPlayer(p).isSpawnSitting());
		}
		
		return endCommand(sender, "§aSpawned all your pets");
	}

}
