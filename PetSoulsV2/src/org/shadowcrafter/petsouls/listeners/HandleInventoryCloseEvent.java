package org.shadowcrafter.petsouls.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.shadowcrafter.petsouls.util.Players;
import org.shadowcrafter.petsouls.util.SoulsPlayer;

public class HandleInventoryCloseEvent implements Listener {
	
	@EventHandler
	public void onPlayerCloseEvent(InventoryCloseEvent e) {
		SoulsPlayer sp = Players.list().getPlayer(e.getPlayer());
		
		if (sp == null) return;
		
		sp.resetInventoryView();
	}

}
