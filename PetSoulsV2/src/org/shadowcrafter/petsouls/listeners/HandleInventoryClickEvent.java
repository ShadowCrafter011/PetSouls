package org.shadowcrafter.petsouls.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.shadowcrafter.petsouls.inventories.Inv;
import org.shadowcrafter.petsouls.pets.PetInterface;
import org.shadowcrafter.petsouls.util.Players;
import org.shadowcrafter.petsouls.util.TemporaryData;

public class HandleInventoryClickEvent implements Listener {
	
	Players list = Players.list();
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		// Inventory mustn't be null
		if (e.getInventory() == null || e.getClickedInventory() == null) return;
		
		// Only if clicked the top inventory
		if (!e.getClickedInventory().equals(e.getView().getTopInventory())) return;
		
		// Is that player in an inventory view not belonging to this plugin
		if (list.getPlayer(p).isAllowedToInteractWithInventory()) return;
		
		if (list.getPlayer(p).getInventoryView() == Inv.MENU) {

			ItemStack item = e.getCurrentItem();
			
			if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && e.getClick() == ClickType.LEFT && item.getItemMeta().hasLore()) {

				try {
					int petNum = Integer.parseInt(item.getItemMeta().getLore().get(0).replaceAll("§7", ""));
					
					for (PetInterface pet : TemporaryData.get().getPets(p)) {
						if (pet.getID() == petNum) {
							pet.toggleExisting();
							
							Players.list().getPlayer(p).openPetsMenu();
						}
					}
					
				}catch (NumberFormatException x) {
					//Wow nothing here
				}
			}
		}
		e.setCancelled(true);
	}

}
