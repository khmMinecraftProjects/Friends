package me.khmdev.Friends.Gestores.Items;

import java.util.List;

import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.Friends.Base;
import me.khmdev.Friends.Gestores.IGestor;
import me.khmdev.Friends.Gestores.InventoryFriendItem;
import me.khmdev.Friends.Gestores.InventoryFriendItem.friendType;
import me.khmdev.Friends.Gestores.ItemGestor;
import me.khmdev.Friends.Gestores.Factorys.IFactoryFriendItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AcceptPeticionItem extends IFriendItem implements IFactoryFriendItem{
	private static AcceptPeticionItem instance;
	public AcceptPeticionItem() {
		super(AuxPlayer.getItem(Material.EMERALD_BLOCK,
				"Aceptar peticion"));
		instance=this;
	}
	@Override
	public IFriendItem create(String pl) {
		return instance;
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public boolean show(friendType type) {
		return friendType.PET==type;
	}
	@Override
	public void execute(Player sender,InventoryFriendItem inventory) {
		String player=inventory.getPlayer();
		IGestor gestor = Base.getInstance().getGestor();
		List<String> l2 = gestor.getPeticiones(sender.getName());

		if (l2.contains(sender.getName())) {
			sender.sendMessage(player
					+ " no te ha enviado ninguna peticion de amistad");
			return;
		}
		gestor.addFriend(player, sender.getName());
		ItemGestor.addFriend(sender.getName(),player);
		sender.sendMessage("Has aceptado la peticion de amistad de "
				+ player);
		sender.openInventory(inventory.getUlt(sender));
	}



}
