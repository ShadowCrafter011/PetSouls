package org.shadowcrafter.petsouls.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.shadowcrafter.petsouls.util.Players;

public class HandleSoulsPlayerEvents implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Players.list().addPlayer(e.getPlayer());
		Players.list().getPlayer(e.getPlayer()).welcome(false);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {		
		Players.list().removePlayer(e.getPlayer());
	}

}
