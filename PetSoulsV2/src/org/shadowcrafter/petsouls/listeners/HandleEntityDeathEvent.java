package org.shadowcrafter.petsouls.listeners;

import java.util.ConcurrentModificationException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.shadowcrafter.petsouls.pets.PetInterface;
import org.shadowcrafter.petsouls.util.TemporaryData;

public class HandleEntityDeathEvent implements Listener {
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		try {
			for (PetInterface pet : TemporaryData.get().getAllPets()) {
				if (pet.isSpawned() && pet.getUUID().compareTo(e.getEntity().getUniqueId()) == 0) {
					pet.removeLife();
				}
			}
		}catch (ConcurrentModificationException x) {
			// Seems to be no problem
		}
	}

}
