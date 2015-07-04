package me.khmdev.Friends.Gestores;

import java.util.HashMap;

public class GestorHeads {
	private static HashMap<String, InventoryFriendItem> heads = new HashMap<>();

	public static InventoryFriendItem getHead(String pl) {
		InventoryFriendItem head = heads.get(pl);
		if (head == null) {
			head = new InventoryFriendItem(pl);
			heads.put(pl, head);
			return head;
		}
		head.update();
		return head;

	}
}
