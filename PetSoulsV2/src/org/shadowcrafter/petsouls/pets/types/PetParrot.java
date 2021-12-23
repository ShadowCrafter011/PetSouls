package org.shadowcrafter.petsouls.pets.types;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Parrot.Variant;
import org.bukkit.inventory.ItemStack;
import org.shadowcrafter.petsouls.PetSouls;
import org.shadowcrafter.petsouls.items.ItemBuilder;
import org.shadowcrafter.petsouls.pets.PetInterface;
import org.shadowcrafter.petsouls.util.Players;
import org.shadowcrafter.petsouls.util.TemporaryData;

@SerializableAs("PetParrot")
public class PetParrot implements PetInterface, ConfigurationSerializable {

	final EntityType type = EntityType.PARROT;
	
	boolean valid = false;
	int id;
	
	UUID owner;
	UUID uuid;
	
	Parrot pet;
	
	int age;
	
	Variant variant;
	String name;
	int lives;
	double health;
	String mods;
	
	boolean spawned = false;	
	
	public PetParrot(Parrot pet) {
		if (pet.isTamed() == false) return;
		
		this.id = PetSouls.getPlugin().addPet();
		
		this.valid = true;
		
		this.owner = pet.getOwner().getUniqueId();
		this.pet = pet;
		
		this.age = pet.getAge();
		
		this.variant = pet.getVariant();
		this.name = pet.getCustomName();
		
		this.lives = 3;
		this.health = pet.getHealth();
		
		this.mods = "mods:";
		
		this.uuid = pet.getUniqueId();
		
		this.spawned = true;
	}
	
	private PetParrot(int id, UUID owner, Variant variant, String name, int lives, double health, String mods, int age, boolean spawned, UUID uuid) {
		this.valid = true;
		
		this.id = id;
		
		this.owner = owner;
		
		this.uuid = uuid;
		
		this.spawned = spawned;
		if (spawned) {
			pet = (Parrot) Bukkit.getEntity(uuid);
		}
		
		this.age = age;
		
		this.variant = variant;
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
		result.put("variant", variant.toString());
		result.put("lives", Integer.toString(lives));
		result.put("health", Double.toString(health));
		result.put("spawned", Boolean.toString(spawned));
		result.put("age", Integer.toString(age));
		result.put("id", Integer.toString(id));
		result.put("uuid", uuid.toString());
		result.put("mods", mods);

		return result;
	}
	
	public static PetParrot deserialize(Map<String, Object> args) {
		return new PetParrot(Integer.parseInt((String) args.get("id")),
				   UUID.fromString((String) args.get("owner")), 
				   Variant.valueOf((String) args.get("variant")),
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
		String name = "§2" + (this.name == null ? "Parrot" : this.name);
		
		ItemStack item = new ItemBuilder(Material.valueOf(type.toString() + "_SPAWN_EGG")).setName(name)
				.setLore("§7" + id,
				" ",
				"§3Lives: " + lives,
				"§3Health: " + ((int) health <= 0 ? "dead" : (int) health),
				"§3Variant: " + variant.toString().toLowerCase(),
				"§3Age: " + (age == 0 ? "adult": "chick"),
				"§3Spawned: " + spawned,
				" ",
				"§5Left Click to " + ((int) health <= 0 ? "revive" : (spawned ? "despawn" : "spawn")) + " this pet")
				.build();
		
		return item;
	}

	@Override
	public UUID getUUID() {
		return uuid;
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
		
		pet = (Parrot) loc.getWorld().spawnEntity(loc, type);
		
		pet.setCustomName(name);
		pet.setTamed(true);
		pet.setOwner((AnimalTamer) Bukkit.getPlayer(owner));
		pet.setVariant(variant);
		pet.setAge(age);
		pet.setSitting(sitting);
		
		boolean revived = false;
		
		if (health <= 0) {
			pet.setHealth(pet.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			revived = true;
		}else {
			pet.setHealth(health);
		}
		
		this.uuid = pet.getUniqueId();
		
		if (message) Bukkit.getPlayer(owner).sendMessage("§a" + (revived ? "revived" : "spawned") + " " + (name == null ? "Parrot" : name));

		this.spawned = true;
	}

	@Override
	public void despawn(boolean message) {
		if (!spawned && !valid) return;
		
		update();
		
		if (message) Bukkit.getPlayer(owner).sendMessage("§aDespawned " + (name == null ? "Parrot" : name));
		pet.remove();
		
		this.spawned = false;
	}

	@Override
	public void save() {
		update();
		
		PetSouls main = PetSouls.getPlugin();
		
		main.getConfig().set("pets." + id, this);
		
		main.saveConfig();
	}

	@Override
	public void update() {
		if (pet != null) {
			this.name = pet.getCustomName();
			this.age = pet.getAge();
			this.variant = pet.getVariant();
			this.health = pet.getHealth();
		}
	}

	@Override
	public void addLife() {
		lives++;
	}

	@Override
	public void removeLife() {
		lives--;
		this.spawned = false;
		
		if (Bukkit.getPlayer(owner) != null && lives > 0) {
			Bukkit.getPlayer(owner).sendMessage("§c" + (name == null ? "Parrot" : name) + " died it has §5" + lives + "§c left. Your pet got despawned for it's own safety");
		}
		
		if (lives == 0) {
			if (PetSouls.getPlugin().getConfig().isSet("pets." + id)) {
				PetSouls.getPlugin().getConfig().set("pets." + id, null);
				PetSouls.getPlugin().saveConfig();
			}
			
			PetSouls.getPlugin().removePet();
			
			TemporaryData.get().removePet(this);
			
			if (Bukkit.getPlayer(owner) != null) {
				Bukkit.getPlayer(owner).sendMessage("§c" + (name == null ? "Parrot" : name) + " died. Because it had §4zero §clives left it traveled to the realm of dead souls");
			}
		}
	}

	@Override
	public Entity getEntity() {
		return pet;
	}

	@Override
	public String getName() {
		return name;
	}

}
