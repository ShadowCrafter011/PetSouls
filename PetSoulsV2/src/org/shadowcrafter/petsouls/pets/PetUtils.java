package org.shadowcrafter.petsouls.pets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import org.shadowcrafter.petsouls.pets.types.PetWolf;

public class PetUtils {
	
	public static PetInterface getRightType(Entity pet) {
		switch (pet.getType()) {
		case WOLF:
			return new PetWolf((Wolf) pet);
		
		default:
			return null;
		}
	}

}
