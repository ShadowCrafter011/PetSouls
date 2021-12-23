package org.shadowcrafter.petsouls.pets;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public interface PetInterface extends ConfigurationSerializable {
	
	public EntityType getType();
	
	public UUID getOwner();
	
	public int getID();
	
	public boolean isValid();
	
	public boolean isSpawned();
	
	public ItemStack getPetItem();
	
	public UUID getUUID();
	
	public void toggleExisting();
	
	public void spawn(Location loc, boolean message, boolean sitting);
	
	public void despawn(boolean message);
	
	public void save();
	
	public void update();
	
	public void addLife();
	
	public void removeLife();
	
	public Entity getEntity();
	
	public String getName();

}
