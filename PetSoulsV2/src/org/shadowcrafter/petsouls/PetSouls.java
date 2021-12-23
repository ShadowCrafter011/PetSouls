package org.shadowcrafter.petsouls;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.shadowcrafter.petsouls.commands.DespawnAllCommand;
import org.shadowcrafter.petsouls.commands.DisablePluginCommand;
import org.shadowcrafter.petsouls.commands.SpawnAllCommand;
import org.shadowcrafter.petsouls.commands.ViewRecipeCommand;
import org.shadowcrafter.petsouls.commands.tabcompleters.NullTabCompleter;
import org.shadowcrafter.petsouls.commands.tabcompleters.ViewRecipeTabCompleter;
import org.shadowcrafter.petsouls.items.NamespacedKeys;
import org.shadowcrafter.petsouls.items.Recipes;
import org.shadowcrafter.petsouls.listeners.HandleEntityDeathEvent;
import org.shadowcrafter.petsouls.listeners.HandleInventoryClickEvent;
import org.shadowcrafter.petsouls.listeners.HandleInventoryCloseEvent;
import org.shadowcrafter.petsouls.listeners.HandlePlayerInteractEntityEvent;
import org.shadowcrafter.petsouls.listeners.HandlePlayerInteractEvent;
import org.shadowcrafter.petsouls.listeners.HandlePrepareItemCraft;
import org.shadowcrafter.petsouls.listeners.HandleSoulsPlayerEvents;
import org.shadowcrafter.petsouls.pets.PetInterface;
import org.shadowcrafter.petsouls.pets.types.PetCat;
import org.shadowcrafter.petsouls.pets.types.PetWolf;
import org.shadowcrafter.petsouls.util.Players;
import org.shadowcrafter.petsouls.util.TemporaryData;

public class PetSouls extends JavaPlugin {
	
	private static PetSouls plugin;
	
	private int numPets;
	
	private int taskID;
	
	static {
		ConfigurationSerialization.registerClass(PetWolf.class, "PetWolf");
		ConfigurationSerialization.registerClass(PetCat.class, "PetCat");
	}
	
	public void onEnable() {
		plugin = this;
		
		registerCommand("disablepetsouls", new DisablePluginCommand(), new NullTabCompleter());
		
		if (!getConfig().isSet("enabled")) {
			getConfig().set("enabled", true);
			saveConfig();
		}else if (!getConfig().getBoolean("enabled")) {
			System.out.println("PetSouls is disabled self destroying...");
			return;
		}
		
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
		
		TemporaryData.get();
		
		System.out.println("[PetSouls] loading pets");
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			int current = 1;
			int amount = getConfig().getInt("numpets");
			int loadedPets = 0;
			
			@Override
			public void run() {
				if (getConfig().isSet("pets." + current)) {
					PetInterface pet = (PetInterface) getConfig().get("pets." + current);
					TemporaryData.get().addPet(pet);
					current++;
					loadedPets++;
					if (loadedPets % 5 == 0) System.out.println("[PetSouls] loaded " + 100/amount*(loadedPets) + "%");
				}else if (loadedPets == amount) {
					System.out.println("[PetSouls] finished loading pets");
					enableDependencies();
					Bukkit.getScheduler().cancelTask(taskID);
				}else {
					current++;
				}
			}
		}, 0, 1);
	}
	
	private void enableDependencies() {
		PluginManager pl = Bukkit.getPluginManager();
		
		pl.registerEvents(new HandlePlayerInteractEntityEvent(), plugin);
		pl.registerEvents(new HandleInventoryClickEvent(), plugin);
		pl.registerEvents(new HandleInventoryCloseEvent(), plugin);
		pl.registerEvents(new HandlePrepareItemCraft(), plugin);
		pl.registerEvents(new HandleSoulsPlayerEvents(), plugin);
		pl.registerEvents(new HandlePlayerInteractEvent(), plugin);
		pl.registerEvents(new HandleEntityDeathEvent(), plugin);
		
		NullTabCompleter Null = new NullTabCompleter();
		
		registerCommand("viewrecipe", new ViewRecipeCommand(), new ViewRecipeTabCompleter());
		registerCommand("spawnallpets", new SpawnAllCommand(), Null);
		registerCommand("despawnallpets", new DespawnAllCommand(), Null);
	}
	
	private void registerCommand(String command, CommandExecutor cmd, TabCompleter tab) {
		getCommand(command).setExecutor(cmd);
		getCommand(command).setTabCompleter(tab);
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
	
	public void removePet() {
		numPets--;
	}
	
	public static PetSouls getPlugin() {
		return plugin;
	}

}
