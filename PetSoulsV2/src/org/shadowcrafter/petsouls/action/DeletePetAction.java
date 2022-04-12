package org.shadowcrafter.petsouls.action;

import org.bukkit.Bukkit;
import org.shadowcrafter.petsouls.pets.PetInterface;
import org.shadowcrafter.petsouls.util.StringUtils;

public class DeletePetAction implements ConfirmableAction {

	PetInterface pet;
	
	public DeletePetAction(PetInterface pet) {
		this.pet = pet;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			pet.removeLife(false);
			if (pet.getLives() == 0) {
				String name = pet.getName() == null ? StringUtils.firstUpperCase(pet.getType().toString().toLowerCase()) : pet.getName();
				Bukkit.getPlayer(pet.getOwner()).sendMessage("§8" + name + " travelled to the realm of dead souls, following your commandment. May that brave and loyal soul rest in peace");
				break;
			}
		}
	}

}
