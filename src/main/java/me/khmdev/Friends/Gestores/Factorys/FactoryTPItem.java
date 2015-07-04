package me.khmdev.Friends.Gestores.Factorys;

import java.util.HashMap;

import me.khmdev.Friends.Gestores.InventoryFriendItem.friendType;
import me.khmdev.Friends.Gestores.Items.IFriendItem;
import me.khmdev.Friends.Gestores.Items.tpToPlayer;

public class FactoryTPItem implements IFactoryFriendItem{
	private static HashMap<String, tpToPlayer>map=new HashMap<>();
	@Override
	public IFriendItem create(String pl) {
		tpToPlayer it=map.get(pl);
		if(it==null){
			it=new tpToPlayer(pl);
			map.put(pl, it);
		}
		return it;
	}

	@Override
	public int getPriority() {
		return 2;
	}

	@Override
	public boolean show(friendType type) {
		return type==friendType.FRIEND;
	}
	

}
