package me.khmdev.Friends.Gestores.Factorys;


import me.khmdev.Friends.Gestores.InventoryFriendItem.friendType;
import me.khmdev.Friends.Gestores.Items.IFriendItem;

public interface IFactoryFriendItem {
	public IFriendItem create(String pl);
	public int getPriority();
	public boolean show(friendType type);
}
