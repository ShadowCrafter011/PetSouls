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

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class SoulsPlayer {
	
	private FileConfiguration config = PetSouls.getPlugin().getConfig();
	
	private Player p;
	private Inv inventoryView;
	private int page;
	private boolean spawnSitting;
	private int crammingWarningLevel;
	private boolean noCrammingWarning;
	private boolean welcome;
	
	public void welcome(boolean bypass) {
		if (!welcome && !bypass) return;
		
		welcome = false;
		config.set("players." + p.getUniqueId() + ".welcome", false);
		PetSouls.getPlugin().saveConfig();
		
		TextComponent text1 = new TextComponent("§bWelcome to PetSouls. To get started watch our");
		TextComponent clickable = new TextComponent(" §dTutorial ");
		TextComponent text2 = new TextComponent("§bor use the");
		TextComponent command = new TextComponent(" §d/viewrecipe command");
		TextComponent text3 = new TextComponent("§b. You can view this message again with the");
		TextComponent command2 = new TextComponent("§d /welcome command");
		clickable.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.youtube.com/watch?v=N5iAGUvGWLg"));
		clickable.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder("§aClick to open the tutorial on Youtube").create())));
		command.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/viewrecipe SOUL_REALM"));
		command.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder("§aClick to paste the §5/viewrecipe command§a in chat").create())));
		command2.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/welcome"));
		command2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder("§aClick to paste the §5/welcome command§a in chat").create())));
		
		p.spigot().sendMessage(text1, clickable, text2, command, text3, command2);
	}
	
	public int getCrammingWarningLevel() {
		return crammingWarningLevel;
	}
	
	public void addCrammingWarningLevel() {
		crammingWarningLevel++;
	}
	
	public void resetCrammingWarningLevel() {
		crammingWarningLevel = 0;
	}

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
		this.crammingWarningLevel = 0;
		
		if (!config.isSet("players." + p.getUniqueId() + ".spawnsitting")) {
			this.spawnSitting = false;
			config.set("players." + p.getUniqueId() + ".spawnsitting", false);
			PetSouls.getPlugin().saveConfig();
		}else {
			this.spawnSitting = config.getBoolean("players." + p.getUniqueId() + ".spawnsitting");
		}
		
		if (!config.isSet("players." + p.getUniqueId() + ".crammingwarning")) {
			this.noCrammingWarning = false;
			config.set("players." + p.getUniqueId() + ".crammingwarning", true);
			PetSouls.getPlugin().saveConfig();
		}else {
			this.noCrammingWarning = config.getBoolean("players." + p.getUniqueId() + ".crammingwarning");
		}
		
		if (!config.isSet("players." + p.getUniqueId() + ".welcome")) {
			this.welcome = true;
			config.set("players." + p.getUniqueId() + ".welcome", true);
			PetSouls.getPlugin().saveConfig();
		}else {
			welcome = config.getBoolean("players." + p.getUniqueId() + ".welcome");
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
		
		if (pets.size() == 0) {
			p.sendMessage("§3You don't have any pets bound to your souls yet. Use the Soul stone for that §5(Recipe: /viewrecipe SOUL_STONE)");
			return;
		}
		
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
		}else if (page != 1) {
			inv.setItem(39, previous);
		}
		
		ItemStack spawnSittingItem = new ItemBuilder(Material.valueOf((spawnSitting ? "LIME" : "GRAY") + "_DYE")).setName("§2Spawn pets in sitting position").setLore(" ", "§aClick to toggle", " ").build();
		ItemStack crammingWarning = new ItemBuilder(Material.valueOf((noCrammingWarning ? "GRAY" : "LIME") + "_DYE")).setName("§2Show entity cramming warning").setLore(" ", "§aClick to toggle", " ").build();
		
		inv.setItem(26, spawnSittingItem);
		inv.setItem(4, crammingWarning);
		
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

	public boolean isNoCrammingWarning() {
		return noCrammingWarning;
	}

	public void setNoCrammingWarning(boolean noCrammingWarning) {
		this.noCrammingWarning = noCrammingWarning;
		
		config.set("players." + p.getUniqueId() + ".crammingwarning", noCrammingWarning);
		PetSouls.getPlugin().saveConfig();
		
		p.sendMessage("§aEntity cramming warning " + (noCrammingWarning ? "disabled" : "enabled"));
		
		update();
	}

}
