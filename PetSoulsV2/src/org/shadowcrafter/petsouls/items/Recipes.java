package org.shadowcrafter.petsouls.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.shadowcrafter.petsouls.inventories.Inv;
import org.shadowcrafter.petsouls.inventories.Inventories;
import org.shadowcrafter.petsouls.util.InventoryUtils;

public class Recipes {
	
	private ItemStack soul_stone;
	private ItemStack soul_realm;
	
	private List<ItemStack> items;
	private Map<ItemStack, ItemKey> keymap;
	private Map<NamespacedKey, String> shapeMap;
	private Map<NamespacedKey, ShapedRecipe> recipiesMap;
	
	private static Recipes recipies;
	
	private Recipes() {
		soul_stone = new ItemBuilder(Material.HEART_OF_THE_SEA).setName("§5Soul Stone").setLore(" ", "§3Used to bind pet souls to yourself", "§3Right click your pets with this item", " ").addEnchant(Enchantment.ARROW_INFINITE, 1).hideEnchants().build();
		soul_realm = new ItemBuilder(Material.AMETHYST_SHARD).setName("§2Realm of Pet Souls").setLore(" ", "§3The place where the souls of", "§3your pets live", "§3[Right Click]", " ").addEnchant(Enchantment.ARROW_INFINITE, 1).hideEnchants().build();
		
		items = new ArrayList<>();
		addItemsToList(items, soul_stone, soul_realm);
		
		ShapedRecipe soulStoneR = new ShapedRecipe(NamespacedKeys.getKey(ItemKey.SOUL_STONE), soul_stone);
		ShapedRecipe soulRealmR = new ShapedRecipe(NamespacedKeys.getKey(ItemKey.SOUL_REALM), soul_realm);
		
		keymap = new HashMap<>();
		
		keymap.put(soul_realm, ItemKey.SOUL_REALM);
		keymap.put(soul_stone, ItemKey.SOUL_STONE);
		
		recipiesMap = new HashMap<>();
		recipiesMap.put(NamespacedKeys.getKey(ItemKey.SOUL_REALM), soulRealmR);
		recipiesMap.put(NamespacedKeys.getKey(ItemKey.SOUL_STONE), soulStoneR);
		
		shapeMap = new HashMap<>();
		
		shapeMap.put(NamespacedKeys.getKey(ItemKey.SOUL_STONE), "TSTBHCTST");
		soulStoneR.shape("TST", "BHC", "TST");
		soulStoneR.setIngredient('T', Material.SOUL_TORCH);
		soulStoneR.setIngredient('S', Material.SOUL_SOIL);
		soulStoneR.setIngredient('B', Material.BONE);
		soulStoneR.setIngredient('H', Material.HEART_OF_THE_SEA);
		soulStoneR.setIngredient('C', Material.COD);
		
		shapeMap.put(NamespacedKeys.getKey(ItemKey.SOUL_REALM), "TETPAPTET");
		soulRealmR.shape("TET", "PAP", "TET");
		soulRealmR.setIngredient('T', Material.SOUL_TORCH);
		soulRealmR.setIngredient('E', Material.ENDER_PEARL);
		soulRealmR.setIngredient('P', Material.ENDER_EYE);
		soulRealmR.setIngredient('A', Material.AMETHYST_SHARD);
		
		Bukkit.addRecipe(soulRealmR);
		Bukkit.addRecipe(soulStoneR);
	}
	
	public static Recipes getRecipes() {
		if (recipies == null) {
			recipies = new Recipes();
			recipies.fillRecipesInventory();
		}
		return recipies;
	}
	
	public Inventory getShape(NamespacedKey key) {
		Inventory output = Bukkit.createInventory(null, 6*9, "§3Recipe for: " + recipiesMap.get(key).getResult().getItemMeta().getDisplayName());
		output = InventoryUtils.fillInventoryWith(output, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build(), false);
		
		ShapedRecipe recipe = recipiesMap.get(key);
		
		String[] shape = shapeMap.get(key).split("");
		Map<Character, ItemStack> shapeMap = recipe.getIngredientMap();
		
		int[] slots = {11, 12, 13, 20, 21, 22, 29, 30, 31, 24};
		output = InventoryUtils.fillSpecificInventorySlotsWith(output, new ItemStack(Material.AIR), slots);
		
		int i = 0;
		for (String s : shape) {
			output.setItem(slots[i], shapeMap.get(s.toCharArray()[0]));
			i++;
		}
		output.setItem(slots[slots.length - 1], recipe.getResult());
		
		ItemStack close = new ItemBuilder(Material.BARRIER).setName("§cClose").build();
		ItemStack back = new ItemBuilder(Material.ARROW).setName("§aBack").build();
		
		output.setItem(48, back);
		output.setItem(49, close);
		
		return output;
	}
	
	public void fillRecipesInventory() {
		Inventory output = Inventories.list().editContents(Inv.RECIPES);
		for (ItemStack item : items) {
			output.setItem(output.firstEmpty(), new ItemBuilder(item).addLoreLine("§9[Click] §aTo view this recipe").addLoreLine("§8##id:" + keymap.get(item)).build());
		}
	}
	
	public ItemStack getSoulStone() {
		return soul_stone;
	}
	
	public ItemStack getSoulRealm() {
		return soul_realm;
	}
	
	public List<ItemStack> getItems(){
		return items;
	}
	
	private void addItemsToList(List<ItemStack> original, ItemStack... items){
		for (ItemStack item : items) {
			original.add(item);
		}
	}
}
