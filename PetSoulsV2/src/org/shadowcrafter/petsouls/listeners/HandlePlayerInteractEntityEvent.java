package org.shadowcrafter.petsouls.listeners;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.shadowcrafter.petsouls.pets.PetUtils;
import org.shadowcrafter.petsouls.util.ItemUtils;
import org.shadowcrafter.petsouls.util.TemporaryData;

public class HandlePlayerInteractEntityEvent implements Listener {

	@EventHandler
	public void onEvent(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		ItemStack mainHand = p.getInventory().getItemInMainHand();
		Entity en = e.getRightClicked();
		
		if (!mainHand.hasItemMeta() || !mainHand.getItemMeta().hasDisplayName()) return;
		
		//if (!ItemUtils.oneMatchesPersistantString("is_petsouls_item", "true", mainHand, offHand)) return;

		if (!ItemUtils.oneDisplayNameMatches("§5Soul Stone", mainHand)) return;

		if (en instanceof Tameable && !((Tameable) en).isTamed() && !((Tameable) en).getOwner().equals((AnimalTamer) p) && en instanceof Axolotl == false) return;

		if (TemporaryData.get().hasTamedPet(p, en.getUniqueId())) return;
		
		TemporaryData.get().addPet(PetUtils.getRightType(en));
		e.setCancelled(true);
		p.sendMessage("added pet");
	}
	
}
