package me.khmdev.Friends.Gestores.Items;

import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.Friends.Gestores.InventoryFriendItem;
import me.khmdev.Friends.Gestores.Factorys.IFactoryFriendItem;
import me.khmdev.Friends.Gestores.InventoryFriendItem.friendType;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BackItem extends IFriendItem implements IFactoryFriendItem{

	private static BackItem instance;
	public BackItem() {
		super(AuxPlayer.getItem(Material.STICK, "Atras"));
		instance=this;
	}
	@Override
	public IFriendItem create(String pl) {
		return instance;
	}

	@Override
	public int getPriority() {
		return -Integer.MAX_VALUE;
	}

	@Override
	public boolean show(friendType type) {
		return true;
	}
	@Override
	public void execute(Player sender, InventoryFriendItem inventory) {
		Inventory inv = inventory.getUlt(sender);
		if (inv != null) {
			sender.openInventory(inv);
		}else{
			sender.closeInventory();
		}
	}

}
