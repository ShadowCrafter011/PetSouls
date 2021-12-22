package org.shadowcrafter.petsouls.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.shadowcrafter.petsouls.items.Recipes;
import org.shadowcrafter.petsouls.util.Players;

public class HandlePlayerInteractEvent implements Listener {
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		
		if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		
		if (e.getItem() == null || !e.getItem().hasItemMeta() || !e.getItem().getItemMeta().hasDisplayName()) return;
		
		if (!e.getItem().getItemMeta().getDisplayName().equals(Recipes.getRecipes().getSoulRealm().getItemMeta().getDisplayName())) return;
		
		e.setCancelled(true);
		Players.list().getPlayer(e.getPlayer()).openPetsMenu();
	}

}
