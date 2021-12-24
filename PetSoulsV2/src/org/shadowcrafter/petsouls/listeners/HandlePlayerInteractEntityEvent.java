package org.shadowcrafter.petsouls.listeners;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.shadowcrafter.petsouls.pets.PetInterface;
import org.shadowcrafter.petsouls.pets.PetUtils;
import org.shadowcrafter.petsouls.util.ItemUtils;
import org.shadowcrafter.petsouls.util.StringUtils;
import org.shadowcrafter.petsouls.util.TemporaryData;

public class HandlePlayerInteractEntityEvent implements Listener {

	@EventHandler
	public void onEvent(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		ItemStack mainHand = p.getInventory().getItemInMainHand();
		Entity en = e.getRightClicked();
		
		if (!mainHand.hasItemMeta() || !mainHand.getItemMeta().hasDisplayName()) return;

		if (!ItemUtils.oneDisplayNameMatches("§5Soul Stone", mainHand)) return;
		
		if (en instanceof Tameable && ((Tameable) en).isTamed() == false) return;

		if (!((Tameable) en).getOwner().equals((AnimalTamer) p)) return;

		if (TemporaryData.get().hasTamedPet(p, en.getUniqueId())) return;
		
		PetInterface pet = PetUtils.getRightType(en);
		
		if (pet == null) {
			p.sendMessage("§cThis Entity either can't be added as pet or isn't supported yet");
			return;
		}
		
		TemporaryData.get().addPet(pet);
		e.setCancelled(true);
		p.sendMessage("§aYou bound §5" + (pet.getName() == null ? StringUtils.firstUpperCase(pet.getType().toString().toLowerCase()) : pet.getName()) + " §ato your soul");
	}
	
}
