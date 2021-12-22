package org.shadowcrafter.petsouls.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.shadowcrafter.petsouls.items.ItemBuilder;
import org.shadowcrafter.petsouls.items.Recipes;
import org.shadowcrafter.petsouls.util.ItemUtils;

public class HandlePrepareItemCraft extends ItemUtils implements Listener {
	
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent e) {	
		if (e.getRecipe() == null) return;
		if (oneMatches(e.getRecipe().getResult(), Recipes.getRecipes().getItems())) {
			e.getInventory().setResult(new ItemBuilder(e.getInventory().getResult()).addRandomNBTTag().addNBTString("true", "is_petsouls_item").build());
		}
		for (ItemStack item : e.getInventory().getMatrix()) {
			if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
				if (oneDisplayNameMatches(item.getItemMeta().getDisplayName(), Recipes.getRecipes().getItems())) {
					e.getInventory().setResult(new ItemStack(Material.AIR));
				}
			}
		}
	}
	
	@EventHandler
	public void onItemCraft(CraftItemEvent e) {
		for (ItemStack item : e.getInventory().getMatrix()) {
			if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
				if (oneDisplayNameMatches(item.getItemMeta().getDisplayName(), Recipes.getRecipes().getItems())) {
					e.setCancelled(true);
				}
			}
		}
	}
}
