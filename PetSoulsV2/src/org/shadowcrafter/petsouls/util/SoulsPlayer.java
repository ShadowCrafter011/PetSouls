package org.shadowcrafter.petsouls.util;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.shadowcrafter.petsouls.PetSouls;
import org.shadowcrafter.petsouls.inventories.Inv;
import org.shadowcrafter.petsouls.inventories.Inventories;
import org.shadowcrafter.petsouls.items.ItemBuilder;
import org.shadowcrafter.petsouls.items.NamespacedKeys;
import org.shadowcrafter.petsouls.items.Recipes;
import org.shadowcrafter.petsouls.pets.PetInterface;

public class SoulsPlayer {
	
	private FileConfiguration config = PetSouls.getPlugin().getConfig();
	
	private Player p;
	private Inv inventoryView;
	private int page;
	private boolean spawnSitting;
	
	public boolean isSpawnSitting() {
		return spawnSitting;
	}

	public void setSpawnSitting(boolean spawnSitting) {
		this.spawnSitting = spawnSitting;
		
		config.set("players." + p.getUniqueId() + ".spawnsitting", spawnSitting);
		PetSouls.getPlugin().saveConfig();
		
		p.sendMessage("§aPet will now spawn " + (spawnSitting ? "sitting" : "standing"));
		
		update();
	}

	public SoulsPlayer(Player p) {
		this.p = p;
		
		if (!config.isSet("players." + p.getUniqueId() + ".spawnsitting")) {
			config.set("players." + p.getUniqueId() + ".spawnsitting", true);
			PetSouls.getPlugin().saveConfig();
		}else {
			this.spawnSitting = config.getBoolean("players." + p.getUniqueId() + ".spawnsitting");
		}
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
	
	public void openPetsMenu(int page) {
		//addNoReset();
		
		if (page < 0) return;
		
		Inventory inv = Bukkit.createInventory(null, 5*9, "§3Your pets §5#" + page);
		
		inv.setContents(Inventories.list().getInventory(Inv.MENU).getContents());
		
		ArrayList<PetInterface> pets = TemporaryData.get().getPets(p);
		
		if (page > Math.ceil((double) pets.size() / 21d)) {
			page = (int) Math.ceil((double) pets.size() / 21d);
		}
		
		//39 41
		ItemStack next = new ItemBuilder(Material.ARROW).setName("§aNext page").build();
		ItemStack previous = new ItemBuilder(Material.ARROW).setName("§aPrevious page").build();
		if (page == 1 && (double) pets.size() / 21d > 1d) {
			inv.setItem(41, next);
		}else if ((double) pets.size() / 21d > (double) page) {
			inv.setItem(41, next);
			inv.setItem(39, previous);
		}else {
			inv.setItem(39, previous);
		}
		
		ItemStack spawnSittingItem = new ItemBuilder(Material.valueOf((spawnSitting ? "LIME" : "GRAY") + "_DYE")).setName("§2Spawn pets in sitting position").setLore(" ", "§aClick to toggle", " ").build();
		
		inv.setItem(26, spawnSittingItem);
		
		for (int i = 0; i < 21; i++) {
			// DO NOT USE RETURN TO BREAK THIS FOR LOOPS!!! (caused me a lot of pain)
			if (i + (page - 1) * 21 >= pets.size()) break;
			
			pets.get(i + (page - 1) * 21).update();
			
			inv.setItem(inv.firstEmpty(), pets.get(i + (page - 1) * 21).getPetItem());
		}
		
		p.openInventory(inv);

		this.inventoryView = Inv.MENU;
		this.page = page;
		
		update();
	}
	
	public void openRecipe(String key) {
		key = key.toUpperCase();
		p.openInventory(Recipes.getRecipes().getShape(NamespacedKeys.getKey(key)));
		
		this.inventoryView = Inv.RECIPE;
		update();
	}
	
	public int getPage() {
		return page;
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
