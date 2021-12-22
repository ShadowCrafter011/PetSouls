package org.shadowcrafter.petsouls;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.shadowcrafter.petsouls.commands.ViewRecipeCommand;
import org.shadowcrafter.petsouls.commands.tabcompleters.ViewRecipeTabCompleter;
import org.shadowcrafter.petsouls.items.NamespacedKeys;
import org.shadowcrafter.petsouls.items.Recipes;
import org.shadowcrafter.petsouls.listeners.HandleInventoryClickEvent;
import org.shadowcrafter.petsouls.listeners.HandleInventoryCloseEvent;
import org.shadowcrafter.petsouls.listeners.HandlePlayerInteractEntityEvent;
import org.shadowcrafter.petsouls.listeners.HandlePlayerInteractEvent;
import org.shadowcrafter.petsouls.listeners.HandlePrepareItemCraft;
import org.shadowcrafter.petsouls.listeners.HandleSoulsPlayerEvents;
import org.shadowcrafter.petsouls.pets.PetInterface;
import org.shadowcrafter.petsouls.pets.types.PetWolf;
import org.shadowcrafter.petsouls.tests.PunchListener;
import org.shadowcrafter.petsouls.tests.SpawnCommand;
import org.shadowcrafter.petsouls.util.Players;
import org.shadowcrafter.petsouls.util.TemporaryData;

public class PetSouls extends JavaPlugin {
	
	private static PetSouls plugin;
	
	private int numPets;
	
	private int taskID;
	
	static {
		ConfigurationSerialization.registerClass(PetWolf.class, "Pet");
	}
	
	public void onEnable() {
		plugin = this;
		
		PluginManager pl = Bukkit.getPluginManager();
		
		pl.registerEvents(new PunchListener(), plugin);
		pl.registerEvents(new HandlePlayerInteractEntityEvent(), plugin);
		pl.registerEvents(new HandleInventoryClickEvent(), plugin);
		pl.registerEvents(new HandleInventoryCloseEvent(), plugin);
		pl.registerEvents(new HandlePrepareItemCraft(), plugin);
		pl.registerEvents(new HandleSoulsPlayerEvents(), plugin);
		pl.registerEvents(new HandlePlayerInteractEvent(), plugin);
		
		if (!getConfig().isSet("numpets")) {
			numPets = 0;
			getConfig().set("numpets", 0);
			saveConfig();
		}else {
			numPets = getConfig().getInt("numpets");
		}
		
		if (!getConfig().isSet("pets")) {
			getConfig().createSection("pets");
			saveConfig();
		}
		
		// Order is important as Recipies needs the NamespacedKeys class
		NamespacedKeys.initialiseKeys();
		// Initialise Recipies
		Recipes.getRecipes();
		Players.list();
		
		getCommand("spawn").setExecutor(new SpawnCommand());
		getCommand("viewrecipe").setExecutor(new ViewRecipeCommand());
		getCommand("viewrecipe").setTabCompleter(new ViewRecipeTabCompleter());
		
		TemporaryData.get();
		
		System.out.println("[PetSouls] loading pets");
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			int current = 1;
			int amount = getConfig().getInt("numpets");
			
			@Override
			public void run() {
				if (getConfig().isSet("pets." + current)) {
					PetInterface pet = (PetInterface) getConfig().get("pets." + current);
					TemporaryData.get().addPet(pet);
					current++;
					System.out.println("[PetSouls] loaded " + 100/amount*(current - 1) + "%");
				}else {
					System.out.println("[PetSouls] finished loading pets");
					Bukkit.getScheduler().cancelTask(taskID);
				}
			}
		}, 0, 3);
	}
	
	public void onDisable() {
		getConfig().set("numpets", numPets);
		
		for (PetInterface pet : TemporaryData.get().getAllPets()) {
			pet.save();
		}
		saveConfig();
	}
	
	public int getPetsAmount() {
		return numPets;
	}
	
	public int addPet() {
		numPets++;
		return numPets;
	}
	
	public static PetSouls getPlugin() {
		return plugin;
	}

}
