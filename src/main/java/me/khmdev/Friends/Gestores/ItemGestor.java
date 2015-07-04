package me.khmdev.Friends.Gestores;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItemNULL;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.Friends.Base;
import me.khmdev.Friends.Gestores.Inventory.FriendInventory;
import me.khmdev.Friends.Gestores.Inventory.PeticionInventory;

public class ItemGestor extends CustomItem {

	public static final ItemStack ant=AuxPlayer.getItem(Material.REDSTONE_BLOCK, "Pagina anterior"),
			sig=AuxPlayer.getItem(Material.EMERALD_BLOCK, "Siguiente pagina");
	public static final CustomItem air=new CustomItemNULL(),
							bars=new CustomItemNULL( AuxPlayer.getItem(Material.IRON_FENCE, " "));
	public static final ItemStack friend = AuxPlayer.getItem(Material.SKULL_ITEM, "Amigos", 3);
	public static final ItemStack peticion = AuxPlayer.getItem(Material.SKULL_ITEM, "Peticiones", 4);
	private List<Player> tmp = new LinkedList<>();

	public ItemGestor() {
		super(AuxPlayer.getItem(Material.SKULL_ITEM, "Gestor de amigos", 3));
	}

	@Override
	public void execute(InventoryClickEvent event) {

	}

	public void execute(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof Player) {
			Player pl = (Player) event.getRightClicked();
			tmp.add(event.getPlayer());

			if (Base.getInstance().getGestor()
					.sonAmigos(event.getPlayer().getName(), pl.getName())) {
				event.getPlayer().openInventory(
						GestorHeads.getHead(pl.getName()).getknown());
			} else {
				event.getPlayer().openInventory(
						GestorHeads.getHead(pl.getName()).getUnknown());
			}
		}

	};


	@Override
	public void execute(PlayerInteractEvent event) {
		if (tmp.contains(event.getPlayer())) {
			tmp.remove(event.getPlayer());
			return;
		}
		Inventory inv = FriendInventory.getInventory(event.getPlayer(), 0);
		if (inv == null) {
			event.getPlayer().sendMessage("Aun no tienes amigos");
			return;
		}
		event.getPlayer().openInventory(inv);
	}


	public static void addFriend(String emi, String recp) {
		FriendInventory.addFriend(emi, recp);
		PeticionInventory.addFriend(emi, recp);

	}

	public static void removeFriend(String emi, String recp) {
		FriendInventory.removeFriend(emi, recp);

	}

	public static void removePeticion(String emi, String recp) {
		PeticionInventory.removePeticion(emi, recp);

	}

}
