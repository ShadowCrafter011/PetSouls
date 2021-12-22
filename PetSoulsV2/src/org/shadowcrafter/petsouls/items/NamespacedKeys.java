package org.shadowcrafter.petsouls.items;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.shadowcrafter.petsouls.PetSouls;


public class NamespacedKeys {
	
	private static Map<ItemKey, NamespacedKey> keys;
	
	private NamespacedKeys() {
		keys = new HashMap<>();
		
		keys.put(ItemKey.SOUL_REALM, new NamespacedKey(PetSouls.getPlugin(), "petsouls_soulrealm"));
		keys.put(ItemKey.SOUL_STONE, new NamespacedKey(PetSouls.getPlugin(), "petsouls_soulstone"));
	}
	
	public static NamespacedKeys initialiseKeys() {
		return new NamespacedKeys();
	}
	
	public static NamespacedKey getKey(ItemKey key) {
		return keys.get(key);
	}
	
	public static NamespacedKey getKey(String key) {
		return keys.get(ItemKey.valueOf(key));
	}

}
