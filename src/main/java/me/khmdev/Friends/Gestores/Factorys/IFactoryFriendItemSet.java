package me.khmdev.Friends.Gestores.Factorys;

import me.khmdev.Friends.Gestores.InventoryFriendItem.friendType;

public abstract class IFactoryFriendItemSet implements IFactoryFriendItem{
	private int priority;
	private friendType[] types;
	public IFactoryFriendItemSet(int prior,friendType... t){
		priority=prior;
		types=t;
	}
	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public boolean show(friendType type) {
		for (friendType t : types) {
			if(t==type){return true;}
		}
		return false;
	}

}
