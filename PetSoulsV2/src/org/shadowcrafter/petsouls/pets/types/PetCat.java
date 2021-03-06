package org.shadowcrafter.petsouls.pets.types;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Cat.Type;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.shadowcrafter.petsouls.PetSouls;
import org.shadowcrafter.petsouls.items.ItemBuilder;
import org.shadowcrafter.petsouls.pets.PetInterface;
import org.shadowcrafter.petsouls.util.Players;
import org.shadowcrafter.petsouls.util.TemporaryData;

@SerializableAs("PetCat")
public class PetCat implements PetInterface, ConfigurationSerializable {

	final EntityType type = EntityType.CAT;
	
	boolean valid = false;
	int id;
	
	UUID owner;
	UUID uuid;
	
	Cat pet;
	
	int age;
	
	Type cattype;
	
	DyeColor collar;
	String name;
	int lives;
	double health;
	String mods;	
	
	boolean spawned = false;
	
	@Override
	public Integer getLives() {
		return lives;
	}
	
	@Override
	public void update() {
		if (pet != null) {
			this.name = pet.getCustomName();
			this.age = pet.getAge();
			this.collar = pet.getCollarColor();
			this.health = pet.getHealth();
		}
	}
	
	@Override
	public void toggleSitting() {
		if (spawned) {
			pet.setSitting(!pet.isSitting());
		}else {
			spawn(Bukkit.getPlayer(owner).getLocation(), true, !Players.list().getPlayer(Bukkit.getPlayer(owner)).isSpawnSitting());
		}
	}
	
	public PetCat(Cat pet) {
		if (pet.isTamed() == false) return;
		
		this.id = PetSouls.getPlugin().addPet();
		this.uuid = pet.getUniqueId();
		
		this.valid = true;
		
		this.owner = pet.getOwner().getUniqueId();
		this.pet = pet;
		
		this.age = pet.getAge();
		
		this.cattype = pet.getCatType();
		this.collar = pet.getCollarColor();
		this.name = pet.getCustomName();
		
		this.lives = 9;
		this.health = pet.getHealth();
		
		this.mods = "mods:";
		
		this.spawned = true;
	}
	
	private PetCat(int id, Type cattype, UUID owner, DyeColor collar, String name, int lives, double health, String mods, int age, boolean spawned, UUID uuid) {
		this.valid = true;
		
		this.id = id;
		this.uuid = uuid;
		
		this.owner = owner;
		
		this.spawned = spawned;
		if (spawned) {
			pet = (Cat) Bukkit.getEntity(uuid);
		}
		
		this.age = age;
		
		this.cattype = cattype;
		
		this.collar = collar;
		this.name = name;
		
		this.mods = mods;
		
		this.lives = lives;
		this.health = health;
	}
		
	@Override
	public Map<String, Object> serialize() {
		if (!valid) return null;
		
		Map<String, Object> result = new LinkedHashMap<>();
		
		result.put("owner", owner.toString());
		result.put("name", name);
		result.put("collar", collar.toString());
		result.put("lives", Integer.toString(lives));
		result.put("health", Double.toString(health));
		result.put("spawned", Boolean.toString(spawned));
		result.put("age", Integer.toString(age));
		result.put("id", Integer.toString(id));
		result.put("uuid", uuid.toString());
		result.put("cattype", cattype.toString());
		result.put("mods", mods);

		return result;
	}
	
	public static PetCat deserialize(Map<String, Object> args) {
		return new PetCat(Integer.parseInt((String) args.get("id")),
						   Type.valueOf((String) args.get("cattype")),
						   UUID.fromString((String) args.get("owner")), 
						   DyeColor.valueOf((String) args.get("collar")),
						   (String)	args.get("name"), 
						   Integer.parseInt((String) args.get("lives")), 
						   Double.parseDouble((String) args.get("health")),
						   (String) args.get("mods"),
						   Integer.parseInt((String) args.get("age")),
						   Boolean.parseBoolean((String) args.get("spawned")),
						   UUID.fromString((String) args.get("uuid")));
	}

	@Override
	public EntityType getType() {
		return type;
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
	public ItemStack getPetItem() {
		String name = "?2" + (this.name == null ? "Cat" : this.name);
		
		ItemStack item = new ItemBuilder(Material.valueOf(type.toString() + "_SPAWN_EGG")).setName(name)
				.setLore("?7" + id,
				" ",
				"?3Lives: " + lives,
				"?3Health: " + ((int) health <= 0 ? "dead" : (int) health),
				"?3Type: " + cattype.toString().toLowerCase(),
				"?3Collar: " + collar.toString().toLowerCase(),
				"?3Age: " + (age == 0 ? "adult": "kitten"),
				"?3Spawned: " + spawned,
				"?3Position: " + (spawned ? (pet.isSitting() ? "sitting" : "standing") : "despawned"),
				" ",
				"?9[Left Click] ?2to " + ((int) health <= 0 ? "revive" : (spawned ? "despawn" : "spawn")) + " this pet",
				"?9[Right Click] ?2to make this pet " + (spawned ? (pet.isSitting() ? "stand up" : "sit down") : (Players.list().getPlayer(Bukkit.getPlayer(owner)).isSpawnSitting() ? "spawn and stand up" : "spawn and sit down")),
				"?9[Shift Left Click] ?2To remove this pet (Requires confirmation)")
				.build();
		
		return item;
	}

	@Override
	public UUID getUUID() {
		return pet != null ? pet.getUniqueId() : null;
	}

	@Override
	public void toggleExisting() {
		if (spawned) {
			despawn(true);
		}else {
			spawn(Bukkit.getPlayer(owner).getLocation(), true, Players.list().getPlayer(Bukkit.getPlayer(owner)).isSpawnSitting());
		}
	}

	@Override
	public void spawn(Location loc, boolean message, boolean sitting) {
		if (spawned && !valid) return;
		
		pet = (Cat) loc.getWorld().spawnEntity(loc, type);
		
		pet.setCustomName(name);
		pet.setTamed(true);
		pet.setOwner((AnimalTamer) Bukkit.getPlayer(owner));
		pet.setCollarColor(collar);
		pet.setAge(age);
		pet.setCatType(cattype);
		pet.setSitting(sitting);
		
		boolean revived = false;
		
		if (health <= 0) {
			pet.setHealth(pet.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			revived = true;
		}else {
			pet.setHealth(health);
		}
		
		this.uuid = pet.getUniqueId();
		
		if (message) Bukkit.getPlayer(owner).sendMessage("?a" + (revived ? "revived" : "spawned") + " " + (name == null ? "Cat" : name));

		this.spawned = true;
	}

	@Override
	public void despawn(boolean message) {
		if (!spawned && !valid) return;
		
		update();
		
		if (message) Bukkit.getPlayer(owner).sendMessage("?aDespawned " + (name == null ? "Cat" : name));
		pet.remove();
		
		this.spawned = false;
	}
	
	@Override
	public void addLife() {
		lives++;
	}
	
	@Override
	public void removeLife(boolean message) {
		lives--;
		this.spawned = false;
		
		if (Bukkit.getPlayer(owner) != null && lives > 0 && message) {
			Bukkit.getPlayer(owner).sendMessage("?c" + (name == null ? "Cat" : name) + " died it has ?5" + lives + "?c left. Your pet got despawned for it's own safety");
		}
		
		if (lives == 0) {
			if (PetSouls.getPlugin().getConfig().isSet("pets." + id)) {
				PetSouls.getPlugin().getConfig().set("pets." + id, null);
				PetSouls.getPlugin().saveConfig();
			}
			
			PetSouls.getPlugin().removePet();
			
			TemporaryData.get().removePet(this);
			
			if (Bukkit.getPlayer(owner) != null && message) {
				Bukkit.getPlayer(owner).sendMessage("?c" + (name == null ? "Cat" : name) + " died. Because it had ?4zero ?clives left it traveled to the realm of dead souls");
			}
		}
	}
	
	@Override
	public Entity getEntity() {
		return Bukkit.getEntity(uuid);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void save() {
		update();
		
		PetSouls main = PetSouls.getPlugin();
		
		main.getConfig().set("pets." + id, this);
		
		main.saveConfig();
	}

}
