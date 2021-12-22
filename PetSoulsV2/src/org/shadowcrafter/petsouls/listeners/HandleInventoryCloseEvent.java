package org.shadowcrafter.petsouls.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.shadowcrafter.petsouls.util.Players;

public class HandleInventoryCloseEvent implements Listener {
	
	@EventHandler
	public void onPlayerCloseEvent(InventoryCloseEvent e) {
		if (Players.list().getPlayer(e.getPlayer()) == null) return;
		
		Players.list().getPlayer(e.getPlayer()).resetInventoryView();
	}

}
