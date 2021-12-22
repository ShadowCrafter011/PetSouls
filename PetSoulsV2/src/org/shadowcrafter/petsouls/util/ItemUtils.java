package org.shadowcrafter.petsouls.util;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.shadowcrafter.petsouls.PetSouls;

public class ItemUtils {
	
	public static boolean oneDisplayNameMatches(String displayName, List<ItemStack> items) {
		for (ItemStack item : items) {
			if (matchesDisplayName(displayName, item)) return true;
		}
		return false;
	}
	
	public static boolean oneDisplayNameMatches(String displayName, ItemStack... items) {
		for (ItemStack item : items) {
			if (matchesDisplayName(displayName, item)) return true;
		}
		return false;
	}
	
	public static boolean matchesDisplayName(String displayName, ItemStack item) {
		return item.getItemMeta().getDisplayName().equals(displayName);
	}
	
	public static boolean matchesPersistantString(String key, String value, ItemStack item) {
		if (!item.hasItemMeta()) return false;
		
		NamespacedKey namespacedKey = new NamespacedKey(PetSouls.getPlugin(), key);
		PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
		
		String val = "";
		
		for (NamespacedKey current : container.getKeys()) {
			if (current.equals(namespacedKey)) {
				val = container.get(current, PersistentDataType.STRING);
			}
		}
		
		return val.equals(value);
	}
	
	public static boolean oneMatchesPersistantString(String key, String value, ItemStack... items) {
		boolean matches = false;
		for (ItemStack item : items) {
			if (matchesPersistantString(key, value, item)) matches = true;
		}
		return matches;
	}
	
	public static boolean oneMatches(ItemStack compare, List<ItemStack> items) {
		for (ItemStack item : items) {
			if (item.equals(compare)) return true;
		}
		return false;
	}

}
