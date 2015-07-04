package me.khmdev.Friends.Gestores.Inventory;

import java.util.HashMap;
import java.util.List;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CItems;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomInventory;
import me.khmdev.Friends.Base;
import me.khmdev.Friends.Gestores.GestorHeads;
import me.khmdev.Friends.Gestores.InventoryFriendItem;
import me.khmdev.Friends.Gestores.InventoryFriendItem.friendType;
import me.khmdev.Friends.Gestores.ItemGestor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PeticionInventory extends CustomInventory {
	private static HashMap<String, PeticionInventory> pet = new HashMap<>();

	public PeticionInventory(String s) {
		super(s);
	}

	public static Inventory getInventory(Player pl, int principio) {
		List<String> friends = Base.getInstance().getGestor()
				.getPeticiones(pl.getName());
		if (principio != 0 && (principio < 0 || principio >= friends.size())) {
			return null;
		}
		PeticionInventory custom = pet.get(pl.getName());
		if (custom == null) {
			custom = new PeticionInventory("Peticiones de " + pl.getName());
			CItems.addInventorys(custom);
			Inventory inv = Bukkit.createInventory(pl, 6 * 9,
			"Peticiones de "
			+ pl.getName());
			custom.setInventory(inv);
		}
		custom.updateAll(pl, principio, friends);
		return custom.getInventory();
	}

	public void updateAll(Player pl, int principio, List<String> friends) {

		int i = principio;
		clear();

		int fin = principio + 4 * 9;
		while (principio >= 0 && principio < fin) {
			if (principio < friends.size()) {
				String s = friends.get(principio);

				InventoryFriendItem head = GestorHeads.getHead(s);
				addItem(head);

			} else {
				addItem(ItemGestor.air);
			}
			principio++;
		}
		for (int j = 0; j < 9; j++) {
			addItem(ItemGestor.bars);
		}

		ItemOpenInventoryFriend anterior = new ItemOpenInventoryFriend(ItemGestor.ant,pl,friendType.PET, i - 4 * 9),
		siguiente = new ItemOpenInventoryFriend(ItemGestor.sig,pl,friendType.PET, i + 4 * 9);
		addItem(anterior);
		addItem(anterior);
		addItem(anterior);
		addItem(anterior);
		
		addItem(new ItemOpenInventoryFriend(ItemGestor.friend, pl,friendType.FRIEND,0));

		addItem(siguiente);
		addItem(siguiente);
		addItem(siguiente);
		addItem(siguiente);
	}
	public static void addFriend(String emi, String recp) {
		if (pet.get(emi) != null) {
			pet.get(emi).remove(GestorHeads.getHead(recp));
		}
	}


	public static void removePeticion(String emi, String recp) {
		if (pet.get(emi) != null) {
			pet.get(emi).remove(GestorHeads.getHead(recp));
		}
	}

}
