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
	
	private Inventories() {
		invs = new HashMap<>();
		
		ItemStack gray = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build();
		@SuppressWarnings("unused")
		ItemStack close = new ItemBuilder(Material.BARRIER).setName("§cClose").build();
		
		Inventory menu = InventoryUtils.fillBorders(Bukkit.createInventory(null, 5*9, "§3Your pets"), gray);
		
		invs.put(Inv.MENU, menu);
	}
	
	public static Inventories list() {
		if (c == null) c = new Inventories();
		return c;
	}
	
	public Inventory getInventory(Inv inv) {
		return invs.get(inv);
	}

}
