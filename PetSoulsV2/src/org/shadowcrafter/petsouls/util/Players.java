package org.shadowcrafter.petsouls.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class Players {
	
	private static Map<Player, SoulsPlayer> players;
	
	private static Players c;
	
	private Players() {
		players = new HashMap<>();
		
		for (Player current : Bukkit.getOnlinePlayers()) {
			if (!players.containsKey(current)) {
				players.put(current, new SoulsPlayer(current));
			}
		}
		
	}
	
	public static Players list() {
		if (c == null) c = new Players();
		return c;
	}
	
	public SoulsPlayer getPlayer(Player p) {
		return players.get(p);
	}
	
	public SoulsPlayer getPlayer(HumanEntity p) {
		return players.get(p);
	}
	
	public List<SoulsPlayer> getPlayersList() {
		List<SoulsPlayer> output = new ArrayList<>();
		for (SoulsPlayer p : players.values()) output.add(p);
		return output;
	}
	
	public void addPlayer(Player p) {
		players.put(p, new SoulsPlayer(p));
	}
	
	public void updatePlayer(SoulsPlayer updatedPlayer) {
		players.put(updatedPlayer.getPlayer(), updatedPlayer);
	}
	
	public void removePlayer(Player p) {
		players.remove(p);
	}

}
