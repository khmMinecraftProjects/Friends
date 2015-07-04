package me.khmdev.Friends.Gestores.Inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomInventory;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;
import me.khmdev.Friends.Gestores.InventoryFriendItem;
import me.khmdev.Friends.Gestores.Items.IFriendItem;

public class FriendCustomInventory extends CustomInventory {
	private InventoryFriendItem gestor;
	public FriendCustomInventory(String s, InventoryFriendItem gest) {
		super(s.replace("%PLAYER%", gest.getPlayer()));
		gestor=gest;
	}

	public void Request(InventoryClickEvent event) {
		
		if (items == null || items.size() <= event.getSlot()) {
			return;
		}
		event.setCancelled(true);

		if (items.get(event.getSlot()) == null||!(items.get(event.getSlot()) instanceof IFriendItem)) {
			return;
		}
		IFriendItem item = (IFriendItem) items.get(event.getSlot());
		if (item.hasPerms(event.getWhoClicked())) {
			item.execute((Player)event.getWhoClicked(),gestor);
		}

	}

	public void addItem(CustomItem item) {
		if (item instanceof IFriendItem) {
			super.addItem(item);
		}
	}

	public void addCustom(CustomItem custom) {
		if (custom instanceof IFriendItem) {
			super.addCustom(custom);
		}
	}

	public void update() {
		for (CustomItem it : items) {
			if (it instanceof IFriendItem) {
				((IFriendItem) it).update();
			}
		}
	}

}
