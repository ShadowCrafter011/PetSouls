package org.shadowcrafter.petsouls.pets.types;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.shadowcrafter.petsouls.PetSouls;
import org.shadowcrafter.petsouls.items.ItemBuilder;
import org.shadowcrafter.petsouls.pets.PetInterface;
import org.shadowcrafter.petsouls.util.TemporaryData;

@SerializableAs("PetWolf")
public class PetWolf implements PetInterface, ConfigurationSerializable {
	
	final EntityType type = EntityType.WOLF;
	
	boolean valid = false;
	int id;
	
	UUID owner;
	
	Wolf pet;
	
	int age;
	
	DyeColor collar;
	String name;
	int lives;
	double health;
	
	boolean spawned = false;	
	
	public PetWolf(Wolf pet) {
		if (pet.isTamed() == false) return;
		
		this.id = PetSouls.getPlugin().addPet();
		
		this.valid = true;
		
		this.owner = pet.getOwner().getUniqueId();
		this.pet = pet;
		
		this.age = pet.getAge();
		
		this.collar = pet.getCollarColor();
		this.name = pet.getCustomName();
		
		this.lives = 5;
		this.health = pet.getHealth();
		
		this.spawned = true;
	}
	
	private PetWolf(int id, UUID owner, DyeColor collar, String name, int lives, double health, int age, boolean spawned, UUID uuid) {
		this.valid = true;
		
		this.id = id;
		
		this.owner = owner;
		
		this.spawned = spawned;
		if (spawned) {
			pet = (Wolf) Bukkit.getEntity(uuid);
		}
		
		this.age = age;
		
		this.collar = collar;
		this.name = name;
		
		this.lives = lives;
		this.health = health;
	}
	
	@Override
	public UUID getOwner() {
		return owner;
	}
	
	@Override
	public int getID() {
		return id;
	}
	
	@Override
	public boolean isValid() {
		return valid;
	}
	
	@Override
	public boolean isSpawned() {
		return spawned;
	}
	
	@Override
	public UUID getUUID() {
		return pet != null ? pet.getUniqueId() : null;
	}
	
	@Override
	public ItemStack getPetItem() {
		String name = "§2" + (this.name == null ? "Wolf" : this.name);
		
		ItemStack item = new ItemBuilder(Material.valueOf(type.toString() + "_SPAWN_EGG")).setName(name).setLore("§7" + id, " ", "§3Lives: " + lives, "§3Health: " + health, "§3Collar: " + collar.toString().toLowerCase(), "§3Age: " + (age == 0 ? "adult": "pup"), "§3Spawned: " + spawned, " ", "§5Left Click to " + (spawned ? "despawn" : "spawn") + " this pet").build();
		
		return item;
	}
	
	@Override
	public EntityType getType() {
		return type;
	}
	
	@Override
	public void toggleExisting() {
		if (spawned) {
			despawn();
		}else {
			spawn(Bukkit.getPlayer(owner).getLocation());
		}
	}
	
	@Override
	public void spawn(Location loc) {
		if (spawned && !valid) return;
		
		pet = (Wolf) loc.getWorld().spawnEntity(loc, type);
		
		pet.setCustomName(name);
		pet.setTamed(true);
		pet.setOwner((AnimalTamer) Bukkit.getPlayer(owner));
		pet.setCollarColor(collar);
		pet.setHealth(health);
		pet.setAge(age);
		
		TemporaryData.get().addSpawnedPet(pet);
		
		Bukkit.getPlayer(owner).sendMessage("§aSpawned " + (name == null ? "Wolf" : name));

		this.spawned = true;
	}

	@Override
	public void despawn() {
		if (!spawned && !valid) return;
		
		TemporaryData.get().removeSpawnedPet(pet);
		
		Bukkit.getPlayer(owner).sendMessage("§aDespawned " + (name == null ? "Wolf" : name));
		pet.remove();
		
		this.spawned = false;
	}

	@Override
	public Map<String, Object> serialize() {
		if (!valid) return null;
		
		Map<String, Object> result = new LinkedHashMap<>();
		
		result.put("owner", owner.toString());
		result.put("name", pet.getCustomName());
		result.put("collar", collar.toString());
		result.put("lives", Integer.toString(lives));
		result.put("health", Double.toString(pet.getHealth()));
		result.put("spawned", Boolean.toString(spawned));
		result.put("age", Integer.toString(age));
		result.put("id", Integer.toString(id));
		result.put("uuid", pet.getUniqueId().toString());

		return result;
	}
	
	public static PetWolf deserialize(Map<String, Object> args) {
		return new PetWolf(Integer.parseInt((String) args.get("id")),
						   UUID.fromString((String) args.get("owner")), 
						   DyeColor.valueOf((String) args.get("collar")),
						   (String)	args.get("name"), 
						   Integer.parseInt((String) args.get("lives")), 
						   Double.parseDouble((String) args.get("health")),
						   Integer.parseInt((String) args.get("age")),
						   Boolean.parseBoolean((String) args.get("spawned")),
						   UUID.fromString((String) args.get("uuid")));
	}
	
	@Override
	public void save() {
		PetSouls main = PetSouls.getPlugin();
		
		main.getConfig().set("pets." + id, this);
		
		main.saveConfig();
	}

}
