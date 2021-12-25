package org.shadowcrafter.petsouls.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shadowcrafter.petsouls.inventories.Inv;
import org.shadowcrafter.petsouls.util.CommandUtils;
import org.shadowcrafter.petsouls.util.Players;

public class ViewRecipeCommand extends CommandUtils implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player == false) return endCommand(sender, "§cSorry, but only players may use this command");
		Player p = (Player) sender;
		
		if (args.length > 1) return endCommand(p, "§cPlease use §5/viewrecipe <recipe key>");
		
		if (args.length == 1) {
			try {
				Players.list().getPlayer(p).openRecipe(args[0]);
			}catch (Exception e) {
				return endCommand(p, "§cSomething went wrong make sure to use §5/viewrecipe <recipe key>");
			}
			return endCommand(p, "§aOpened recipe for: §3" + p.getOpenInventory().getTitle().replaceFirst("§3Recipe for: ", ""));
		}
		
		if (args.length == 0) {
			Players.list().getPlayer(p).openInventors(Inv.RECIPES);
			return endCommand(sender, "§aOpened the recipes Inventory");
		}
		return endCommand(sender, "§cSomething went wrong");
	}

}
