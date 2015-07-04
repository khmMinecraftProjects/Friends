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

public class FriendInventory extends CustomInventory{
	private static HashMap<String, FriendInventory> amis = new HashMap<>();
	public FriendInventory(String s) {
		super(s);
	}
	public static Inventory getInventory(Player pl, int principio) {
		List<String> friends = Base.getInstance().getGestor()
				.getFriends(pl.getName());
		if (principio != 0 && (principio < 0 || principio >= friends.size())) {
			return null;
		}
		FriendInventory custom = amis.get(pl.getName());
		if (custom == null) {
			custom = new FriendInventory("Amigos de " + pl.getName());
			CItems.addInventorys(custom);
			amis.put(pl.getName(), custom);
			Inventory inv = Bukkit.createInventory(pl, 6 * 9,
					"Amigos de " + pl.getName());
			custom.setInventory(inv);
		}
		custom.updateAll(pl, principio, friends);
		return custom.getInventory();
	}

	public void updateAll(Player pl, int principio, List<String> friends) {
		clear();
		int i = principio;
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
		
		ItemOpenInventoryFriend anterior = new ItemOpenInventoryFriend(ItemGestor.ant, pl,friendType.FRIEND,i - 4 * 9), 
				siguiente = new ItemOpenInventoryFriend(ItemGestor.sig,pl,friendType.FRIEND, i + 4 * 9);
		addItem(anterior);
		addItem(anterior);
		addItem(anterior);
		addItem(anterior);

		addItem(new ItemOpenInventoryFriend(ItemGestor.peticion, pl,friendType.PET,0));

		addItem(siguiente);
		addItem(siguiente);
		addItem(siguiente);
		addItem(siguiente);
	}
	
	public static void addFriend(String emi, String recp) {
		if (amis.get(emi) != null) {
			amis.get(emi).addCustom(GestorHeads.getHead(recp));
		}
		if (amis.get(recp) != null) {
			amis.get(recp).addCustom(GestorHeads.getHead(emi));
		}
	}

	public static void removeFriend(String emi, String recp) {
		if (amis.get(emi) != null) {
			amis.get(emi).remove(GestorHeads.getHead(recp));
		}
		if (amis.get(recp) != null) {
			amis.get(recp).remove(GestorHeads.getHead(emi));
		}
	}



}
