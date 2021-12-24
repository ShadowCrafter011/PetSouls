package org.shadowcrafter.petsouls.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shadowcrafter.petsouls.PetSouls;
import org.shadowcrafter.petsouls.pets.PetInterface;
import org.shadowcrafter.petsouls.util.CommandUtils;
import org.shadowcrafter.petsouls.util.Players;
import org.shadowcrafter.petsouls.util.SoulsPlayer;
import org.shadowcrafter.petsouls.util.TemporaryData;

public class SpawnAllCommand extends CommandUtils implements CommandExecutor {
	
	int taskID;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player == false) return endCommand(sender, "§cSorry, but only players may use this command");
		Player p = (Player) sender;
		
		if (args.length != 0) return endCommand(sender, "§cPlease use §5/spawnallpets");
		
		ArrayList<PetInterface> pets = new ArrayList<>();
		
		SoulsPlayer sp = Players.list().getPlayer(p);
		
		for (PetInterface pet : TemporaryData.get().getPets(p)) {
			if (!pet.isSpawned()) pets.add(pet);
		}
		
		int crammingLimit = p.getWorld().getGameRuleValue(GameRule.MAX_ENTITY_CRAMMING);
		if (pets.size() <= crammingLimit - 1) {
			for (PetInterface pet : TemporaryData.get().getPets(p)) {
				pet.spawn(p.getLocation(), false, sp.isSpawnSitting());
			}
			return endCommand(sender, "§aSpawned all your pets");
		}else {
			
			
			if (sp.getCrammingWarningLevel() == 1 || sp.isNoCrammingWarning()) {
				p.sendMessage("§aCountdown started");
				sp.resetCrammingWarningLevel();
				
				taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(PetSouls.getPlugin(), new Runnable() {
					
					int secondsPassed = 0;
					int index = 0;
					
					@Override
					public void run() {
						secondsPassed += 1;
						
						if (secondsPassed < 5) {
							p.sendMessage("§aSpawning starts in " + (5 - secondsPassed) + (secondsPassed == 4 ? " second" : " seconds"));
						}else if ((secondsPassed - 3) % 2 == 0) {
							for (int i = 0; i < crammingLimit - 1; i++) {
								if (i + index >= pets.size()) {
									p.sendMessage("§aSpawned all your pets");
									Bukkit.getScheduler().cancelTask(taskID);
									break;
								}
								
								pets.get(i + index).spawn(p.getLocation(), false, sp.isSpawnSitting());
							}
							index += crammingLimit;
						}
					}
				}, 20, 20);
			}else if (sp.getCrammingWarningLevel() == 0) {
				sender.sendMessage("§3You have more despawned pets that the entity cramming limit. Your pets will start spawning in 5 seconds in batches of " + p.getWorld().getGameRuleValue(GameRule.MAX_ENTITY_CRAMMING) + " with a 2 second interval. It is advised to move around in that time period to prevent your pets from dying. Run this action again to start the process");
				sp.addCrammingWarningLevel();
			}
		}
		return true;
	}

}
