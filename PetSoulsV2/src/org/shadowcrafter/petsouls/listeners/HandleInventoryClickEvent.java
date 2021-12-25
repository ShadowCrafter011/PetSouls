package org.shadowcrafter.petsouls.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.shadowcrafter.petsouls.action.DeletePetAction;
import org.shadowcrafter.petsouls.inventories.Inv;
import org.shadowcrafter.petsouls.items.ItemBuilder;
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
		
		ItemStack item = e.getCurrentItem();
		
		SoulsPlayer sp = Players.list().getPlayer(p);
		
		if (item == null) {
			e.setCancelled(true);
			return;
		}
		
		switch (list.getPlayer(p).getInventoryView()) {	
		
		case MENU:

			//Settings and Actions
			switch (item.getType()) {
			case ARROW:
				if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) break;
				
				switch (item.getItemMeta().getDisplayName()) {
				case "§aNext page":
					sp.openPetsMenu(sp.getPage() + 1);
					break;
					
				case "§aPrevious page":
					sp.openPetsMenu(sp.getPage() - 1);
					break;
				
				default:
					break;
				}
				
				break;
				
			case BARRIER:
				if (e.getClick() == ClickType.LEFT) {
					p.closeInventory();
				}
				break;
				
			case TORCH:
				p.performCommand("spawnallpets");
				sp.openPetsMenu(sp.getPage());
				break;
				
			case SOUL_TORCH:
				p.performCommand("despawnallpets");
				sp.openPetsMenu(sp.getPage());
				break;
				
			case GRAY_DYE:
				if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) break;
				
				switch (item.getItemMeta().getDisplayName()) {
				case "§2Spawn pets in sitting position":
					sp.setSpawnSitting(true);
					break;
					
				case "§2Show entity cramming warning":
					sp.setNoCrammingWarning(false);
					break;
					
				default:
					break;
				}
				sp.openPetsMenu(sp.getPage());
				
				break;
				
			case LIME_DYE:
				if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) break;
				
				switch (item.getItemMeta().getDisplayName()) {
				case "§2Spawn pets in sitting position":
					sp.setSpawnSitting(false);
					break;
					
				case "§2Show entity cramming warning":
					sp.setNoCrammingWarning(true);
					break;
					
				default:
					break;
				}
				sp.openPetsMenu(sp.getPage());
				
				break;
			
			default:
				break;
			}
			
			//Interactions with pets
			if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName() || !item.getItemMeta().hasLore()) {
				e.setCancelled(true);
				return;
			}
			
			int petNum;
			try {
				petNum = Integer.parseInt(item.getItemMeta().getLore().get(0).replaceAll("§7", ""));
			} catch (NumberFormatException x) {
				e.setCancelled(true);
				return;
			}
			
			PetInterface pet = null;
			for (PetInterface i : TemporaryData.get().getPets(p)) {
				if (i.getID() == petNum) pet = i;
			}
			if (pet == null) {
				e.setCancelled(true);
				return;
			}
			
			switch (e.getClick()) {
			case LEFT:
					pet.toggleExisting();
					sp.openPetsMenu(sp.getPage());
				break;
				
			case RIGHT:
				pet.toggleSitting();
				sp.openPetsMenu(sp.getPage());
				break;
				
			case SHIFT_LEFT:
				sp.setConfirmableAction(new DeletePetAction(pet));
				sp.openInventors(Inv.DELETE);
				break;
			
			default:
				break;
			}
			
			e.setCancelled(true);
			break;
			
		case DELETE:
			if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.RED_WOOL) {
				e.getClickedInventory().setItem(e.getSlot(), new ItemBuilder(Material.LIME_WOOL).setName("§aConfirmed").build());
				
				if (!e.getClickedInventory().contains(Material.RED_WOOL)) {
					sp.runConfirmableAction();
					p.closeInventory();
				}
			}else if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.BARRIER && e.getClick() == ClickType.LEFT) {
				p.closeInventory();
			}
			
			e.setCancelled(true);
			break;
			
		case RECIPES:
			e.setCancelled(true);
			if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
				String line = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1);
				line = line.replaceAll("§8##id:", "");
				sp.openRecipe(line);
				p.sendMessage("§aOpened recipe for: §3" + p.getOpenInventory().getTitle().replaceFirst("§3Recipe for: ", ""));
			}
			break;
			
		case RECIPE:
			e.setCancelled(true);
			if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
				switch (item.getItemMeta().getDisplayName()) {
				case "§aBack":
					p.performCommand("viewrecipe");
					break;
					
				case "§cClose":
					p.closeInventory();
					break;
				
				default:
					break;
				}
			}
			
		default:
			break;
		}
		
	}

}
