package org.shadowcrafter.petsouls.tests;

import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.shadowcrafter.petsouls.PetSouls;
import org.shadowcrafter.petsouls.pets.types.PetWolf;

public class PunchListener implements Listener {
	
	@EventHandler
	public void onPunch(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Wolf) {
			PetSouls.getPlugin().getConfig().set("1", new PetWolf((Wolf) e.getEntity()));
			PetSouls.getPlugin().saveConfig();
			e.getDamager().sendMessage("saved");
		}
	}

}
