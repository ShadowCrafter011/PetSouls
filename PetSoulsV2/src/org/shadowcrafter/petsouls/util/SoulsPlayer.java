package org.shadowcrafter.petsouls.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.shadowcrafter.petsouls.inventories.Inv;
import org.shadowcrafter.petsouls.inventories.Inventories;
import org.shadowcrafter.petsouls.items.NamespacedKeys;
import org.shadowcrafter.petsouls.items.Recipes;
import org.shadowcrafter.petsouls.pets.PetInterface;

public class SoulsPlayer {
	
	private Player p;
	private Inv inventoryView;
	
	public SoulsPlayer(Player p) {
		this.p = p;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public void openInventors(Inv inv) {
		if (inv == Inv.RECIPE) return;
		
		p.openInventory(Inventories.list().getInventory(inv));
		this.inventoryView = inv;
		update();
	}
	
	public void openPetsMenu() {
		Inventory inv = Bukkit.createInventory(null, 5*9, "§3Your pets");
		
		inv.setContents(Inventories.list().getInventory(Inv.MENU).getContents());
		
		for (PetInterface pet : TemporaryData.get().getPets(p)) {
			
			inv.setItem(inv.firstEmpty(), pet.getPetItem());
		}
		
		p.openInventory(inv);
		
		this.inventoryView = Inv.MENU;
		update();
	}
	
	public void openRecipe(String key) {
		key = key.toUpperCase();
		p.openInventory(Recipes.getRecipes().getShape(NamespacedKeys.getKey(key)));
		
		this.inventoryView = Inv.RECIPE;
		update();
	}
	
	public Inv getInventoryView() {
		return inventoryView;
	}
	
	public void setInventoryView(Inv inv) {
		this.inventoryView = inv;
		update();
	}
	
	public void resetInventoryView() {
		this.inventoryView = null;
		update();
	}
	
	public boolean isAllowedToInteractWithInventory() {
		return inventoryView == null;
	}
	
	private void update() {
		Players.list().updatePlayer(this);
	}

}
