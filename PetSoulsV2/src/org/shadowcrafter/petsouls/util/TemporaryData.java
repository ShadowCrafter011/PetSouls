package org.shadowcrafter.petsouls.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.shadowcrafter.petsouls.pets.PetInterface;

public class TemporaryData {
	
	private static TemporaryData data;
	
	Map<UUID, ArrayList<PetInterface>> playerPets;
	List<PetInterface> pets;
	List<Entity> spawnedPets;

	private TemporaryData() {
		playerPets = new HashMap<>();
		spawnedPets = new ArrayList<>();
		pets = new ArrayList<>();
	}
	
	public boolean hasTamedPet(Player p, UUID uuid) {
		if (!playerPets.containsKey(p.getUniqueId())) return false;
		
		for (PetInterface pet : playerPets.get(p.getUniqueId())) {
			if (pet.isValid() && pet.isSpawned() && pet.getUUID().compareTo(uuid) == 0) return true;
		}
		return false;
	}
	
	public void addPet(PetInterface pet) {
		ArrayList<PetInterface> pts = playerPets.containsKey(pet.getOwner()) ? playerPets.get(pet.getOwner()) : new ArrayList<>();
		
		pets.add(pet);
		pts.add(pet);
		
		playerPets.put(pet.getOwner(), pts);
	}
	
	public void removeSpawnedPet(Entity pet) {
		spawnedPets.remove(pet);
	}
	
	public void addSpawnedPet(Entity pet) {
		spawnedPets.add(pet);
	}
	
	public List<PetInterface> getAllPets() {
		return pets;
	}
	
	public ArrayList<PetInterface> getPets(Player p) {
		return playerPets.containsKey(p.getUniqueId()) ? playerPets.get(p.getUniqueId()) : new ArrayList<>();
	}
	
	public static TemporaryData get() {
		if (data == null) data = new TemporaryData();
		return data;
	}
	
}
