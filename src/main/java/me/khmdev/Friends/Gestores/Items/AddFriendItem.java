package me.khmdev.Friends.Gestores.Items;

import java.util.List;

import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.Friends.Base;
import me.khmdev.Friends.Gestores.IGestor;
import me.khmdev.Friends.Gestores.InventoryFriendItem;
import me.khmdev.Friends.Gestores.InventoryFriendItem.friendType;
import me.khmdev.Friends.Gestores.Factorys.IFactoryFriendItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AddFriendItem extends IFriendItem implements IFactoryFriendItem{
	private static AddFriendItem instance;
	public AddFriendItem() {
		super(AuxPlayer.getItem(Material.EMERALD_BLOCK, "Agregar amigo"));
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
		return friendType.UNKNOW==type;
	}

	@Override
	public void execute(Player sender, InventoryFriendItem inventory) {
		String player=inventory.getPlayer();
		IGestor gestor = Base.getInstance().getGestor();
		List<String> l = gestor.getFriends(sender.getName());
		List<String> l2 = gestor.getPeticiones(player);
		List<String> l3 = gestor.getPeticiones(sender.getName());

		if (l.contains(player)) {
			sender.sendMessage("Ya eres amigo de " + player);
			return;
		}
		if (l2.contains(sender.getName())) {
			sender.sendMessage("Ya has enviado una peticion a "
					+ player);
			return;
		}
		if (l3.contains(player)) {
			sender.sendMessage(player+" ya te ha enviado una peticion");
			return;
		}
		gestor.addPeticion(sender.getName(), player);
		sender.sendMessage("Has enviado tu peticion de amistad a "
				+ player);
	}
	
}
