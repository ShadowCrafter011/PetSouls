package org.shadowcrafter.petsouls.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.shadowcrafter.petsouls.inventories.Inv;
import org.shadowcrafter.petsouls.pets.PetInterface;
import org.shadowcrafter.petsouls.util.Players;
import org.shadowcrafter.petsouls.util.SoulsPlayer;
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
			
			SoulsPlayer currentP = Players.list().getPlayer(p);
			
			if (item != null && item.getType() == Material.ARROW) {
				
				switch (item.getItemMeta().getDisplayName()) {
				case "§aNext page":
					currentP.openPetsMenu(currentP.getPage() + 1);
					break;
					
				case "§aPrevious page":
					currentP.openPetsMenu(currentP.getPage() - 1);
					break;
				
				default:
					break;
				}
				
			}
			
			if (item != null && item.getType() == Material.BARRIER) {
				p.closeInventory();
			}
			
			if (item != null && item.getType() == Material.TORCH) {
				p.performCommand("spawnallpets");
				currentP.openPetsMenu(currentP.getPage());
			}
			
			if (item != null && item.getType() == Material.SOUL_TORCH) {
				p.performCommand("despawnallpets");
				currentP.openPetsMenu(currentP.getPage());
			}
			
			if (item != null && item.getType() == Material.GRAY_DYE && item.hasItemMeta()) {
				
				switch (item.getItemMeta().getDisplayName()) {
				case "§2Spawn pets in sitting position":
					currentP.setSpawnSitting(true);
					break;
					
				case "§2Show entity cramming warning":
					currentP.setNoCrammingWarning(false);
					break;
					
				default:
					break;
				}
				
				currentP.openPetsMenu(currentP.getPage());
			}
			
			if (item != null && item.getType() == Material.LIME_DYE && item.hasItemMeta()) {
				
				switch (item.getItemMeta().getDisplayName()) {
				case "§2Spawn pets in sitting position":
					currentP.setSpawnSitting(false);
					break;
					
				case "§2Show entity cramming warning":
					currentP.setNoCrammingWarning(true);
					break;
					
				default:
					break;
				}
				
				currentP.openPetsMenu(currentP.getPage());
			}
			
			if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && e.getClick() == ClickType.LEFT && item.getItemMeta().hasLore()) {

				try {
					int petNum = Integer.parseInt(item.getItemMeta().getLore().get(0).replaceAll("§7", ""));
					
					for (PetInterface pet : TemporaryData.get().getPets(p)) {
						if (pet.getID() == petNum) {
							pet.toggleExisting();
							currentP.openPetsMenu(currentP.getPage());
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
