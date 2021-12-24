package org.shadowcrafter.petsouls.inventories;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.shadowcrafter.petsouls.items.ItemBuilder;
import org.shadowcrafter.petsouls.util.InventoryUtils;

public class Inventories {
	
	private static Inventories c;
	
	private Map<Inv, Inventory> invs;
	private Map<Inv, String> names;
	
	private Inventories() {
		invs = new HashMap<>();
		names = new HashMap<>();
		
		ItemStack gray = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build();
		ItemStack close = new ItemBuilder(Material.BARRIER).setName("§cClose").build();
		ItemStack confirmationPending = new ItemBuilder(Material.RED_WOOL).setName("§cClick to confirm").build();
		
		ItemStack spawn = new ItemBuilder(Material.TORCH).setName("§2Spawn all your pets").build();
		ItemStack despawn = new ItemBuilder(Material.SOUL_TORCH).setName("§cDespawn all your pets").build();
		
		Inventory menu = InventoryUtils.fillBorders(Bukkit.createInventory(null, 5*9, "§3Your pets"), gray);
		Inventory delete = InventoryUtils.fillBorders(Bukkit.createInventory(null, 5*9, "§cConfirm deleting"), gray);
		
		delete.setItem(20, confirmationPending);
		delete.setItem(22, confirmationPending);
		delete.setItem(24, confirmationPending);
		delete.setItem(40, close);
		
		menu.setItem(40, close);
		menu.setItem(3, spawn);
		menu.setItem(5, despawn);
		
		invs.put(Inv.MENU, menu); names.put(Inv.MENU, "§3Your pets");
		invs.put(Inv.DELETE, delete); names.put(Inv.DELETE, "§cConfirm deleting");
	}
	
	public static Inventories list() {
		if (c == null) c = new Inventories();
		return c;
	}
	
	public Inventory getInventory(Inv inv) {
		Inventory output = Bukkit.createInventory(null, invs.get(inv).getSize(), names.get(inv));
		output.setContents(invs.get(inv).getContents());
		return output;
	}

}
