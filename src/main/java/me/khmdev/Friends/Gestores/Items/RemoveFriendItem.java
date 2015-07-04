package me.khmdev.Friends.Gestores.Items;

import java.util.List;

import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.Friends.Base;
import me.khmdev.Friends.Gestores.IGestor;
import me.khmdev.Friends.Gestores.InventoryFriendItem;
import me.khmdev.Friends.Gestores.ItemGestor;
import me.khmdev.Friends.Gestores.Factorys.IFactoryFriendItem;
import me.khmdev.Friends.Gestores.InventoryFriendItem.friendType;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class RemoveFriendItem extends IFriendItem implements IFactoryFriendItem {

	private static RemoveFriendItem instance;

	public RemoveFriendItem() {
		super(AuxPlayer.getItem(Material.REDSTONE_BLOCK, "Eliminar amigo"));
		instance = this;
	}

	@Override
	public IFriendItem create(String pl) {
		return instance;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public boolean show(friendType type) {
		return friendType.KNOW==type||type==friendType.FRIEND;
	}

	@Override
	public void execute(Player sender, InventoryFriendItem inventory) {
		String own = inventory.getPlayer();
		IGestor gestor = Base.getInstance().getGestor();
		List<String> l = gestor.getFriends(sender.getName());
		if (!l.contains(own)) {
			sender.sendMessage("No eres amigo de " + own);
			return;
		}
		gestor.removeFriend(sender.getName(), own);
		ItemGestor.removeFriend(sender.getName(), own);

		sender.sendMessage("Ya no eres amigo de " + own);
	}
}
