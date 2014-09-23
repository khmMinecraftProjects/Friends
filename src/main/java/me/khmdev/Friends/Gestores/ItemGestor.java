package me.khmdev.Friends.Gestores;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CItems;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomInventory;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItemNULL;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.ItemOpenInventory;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.Friends.Base;

public class ItemGestor extends CustomItem {
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
						getHead(pl.getName()).getknown());
			} else {
				event.getPlayer().openInventory(
						getHead(pl.getName()).getUnknown());
			}
		}

	};

	private List<Player> tmp = new LinkedList<>();

	@Override
	public void execute(PlayerInteractEvent event) {
		if (tmp.contains(event.getPlayer())) {
			tmp.remove(event.getPlayer());
			return;
		}
		Inventory inv = getInventory(event.getPlayer(), 0);
		if (inv == null) {
			event.getPlayer().sendMessage("Aun no tienes amigos");
			return;
		}
		event.getPlayer().openInventory(inv);
	}

	private static HashMap<String, CustomInventory> amis = new HashMap<>();
	private static HashMap<String, CustomInventory> pet = new HashMap<>();

	private static HashMap<String, InventoryFriendItem> heads = new HashMap<>();
	private static ItemStack bars = AuxPlayer.getItem(Material.IRON_FENCE, " "),
			ant=AuxPlayer.getItem(Material.REDSTONE_BLOCK, "Pagina anterior"),
			sig=AuxPlayer.getItem(Material.EMERALD_BLOCK, "Siguiente pagina");

	private static Inventory getInventory(Player pl, int principio) {
		return getInventory(pl, principio, null);
	}

	private static Inventory getInventory(Player pl, int principio,
			Inventory inve) {
		List<String> friends = Base.getInstance().getGestor()
				.getFriends(pl.getName());
		if (principio != 0 && (principio < 0 || principio >= friends.size())) {
			return null;
		}
		int i = principio;
		CustomInventory custom = amis.get(pl.getName());
		if (custom == null) {
			custom = new CustomInventory("Amigos de " + pl.getName());
			CItems.addInventorys(custom);
			amis.put(pl.getName(), custom);
			Inventory inv = Bukkit.createInventory(pl, 6 * 9,
					"Amigos de " + pl.getName());
			custom.setInventory(inv);
		}
		custom.clear();

		int fin = principio + 4 * 9;
		while (principio >= 0 && principio < fin) {
			if (principio < friends.size()) {
				String s = friends.get(principio);

				InventoryFriendItem head = getHead(s);
				custom.addItem(head);

			} else {
				custom.addItem(new CustomItemNULL());
			}
			principio++;
		}
		for (int j = 0; j < 9; j++) {
			custom.addItem(new CustomItemNULL(bars));
		}
		ItemOpenInventory anterior = new ItemOpenInventory(ant, getInventory(pl,
				i - 4 * 9, inve)), siguiente = new ItemOpenInventory(sig,
				getInventory(pl, i + 4 * 9, inve));
		custom.addItem(anterior);
		custom.addItem(anterior);
		custom.addItem(anterior);
		custom.addItem(anterior);

		if (inve == null) {
			custom.addItem(new ItemOpenInventory(AuxPlayer.getItem(
					Material.SKULL_ITEM, "Peticiones", 4),
					getInventoryPeticiones(pl, 0, custom.getInventory())));
		} else {
			custom.addItem(new ItemOpenInventory(AuxPlayer.getItem(
					Material.SKULL_ITEM, "Peticiones", 4), inve));
		}

		custom.addItem(siguiente);
		custom.addItem(siguiente);
		custom.addItem(siguiente);
		custom.addItem(siguiente);

		return custom.getInventory();
	}

	private static Inventory getInventoryPeticiones(Player pl, int principio,
			Inventory inve) {
		List<String> friends = Base.getInstance().getGestor()
				.getPeticiones(pl.getName());
		if (principio != 0 && (principio < 0 || principio >= friends.size())) {
			return null;
		}

		int i = principio;
		CustomInventory custom = pet.get(pl.getName());
		if (custom == null) {
			custom = new CustomInventory("Peticiones de " + pl.getName());
			CItems.addInventorys(custom);
			pet.put(pl.getName(), custom);
			Inventory inv = Bukkit.createInventory(pl, 6 * 9, "Peticiones de "
					+ pl.getName());
			custom.setInventory(inv);
		}
		custom.clear();

		int fin = principio + 4 * 9;
		while (principio >= 0 && principio < fin) {
			if (principio < friends.size()) {
				String s = friends.get(principio);

				InventoryFriendItem head = getHead(s);
				custom.addItem(head);

			} else {
				custom.addItem(new CustomItemNULL());
			}
			principio++;
		}
		ItemStack bars = AuxPlayer.getItem(Material.IRON_FENCE, " ");
		for (int j = 0; j < 9; j++) {
			custom.addItem(new CustomItemNULL(bars));
		}
		ItemOpenInventory anterior = new ItemOpenInventory(AuxPlayer.getItem(
				Material.REDSTONE_BLOCK, "Pagina anterior"),
				getInventoryPeticiones(pl, i - 4 * 9, inve)), siguiente = new ItemOpenInventory(
				AuxPlayer.getItem(Material.EMERALD_BLOCK, "Siguiente pagina"),
				getInventoryPeticiones(pl, i + 4 * 9, inve));
		custom.addItem(anterior);
		custom.addItem(anterior);
		custom.addItem(anterior);
		custom.addItem(anterior);
		if (inve == null) {
			custom.addItem(new ItemOpenInventory(AuxPlayer.getItem(
					Material.SKULL_ITEM, "Amigos", 3), getInventory(pl, 0,
					custom.getInventory())));
		} else {
			custom.addItem(new ItemOpenInventory(AuxPlayer.getItem(
					Material.SKULL_ITEM, "Amigos", 3), inve));
		}
		custom.addItem(siguiente);
		custom.addItem(siguiente);
		custom.addItem(siguiente);
		custom.addItem(siguiente);

		return custom.getInventory();
	}

	private static InventoryFriendItem getHead(String pl) {
		InventoryFriendItem head = heads.get(pl);
		if (head == null) {
			head = new InventoryFriendItem(pl);
			heads.put(pl, head);
			return head;
		}
		head.updateServer();
		return head;

	}

	public static void addFriend(String emi, String recp) {
		if (amis.get(emi) != null) {
			amis.get(emi).addCustom(getHead(recp));
		}
		if (amis.get(recp) != null) {
			amis.get(recp).addCustom(getHead(emi));
		}
		if (pet.get(emi) != null) {
			pet.get(emi).remove(getHead(recp));
		}
	}

	public static void removeFriend(String emi, String recp) {
		if (amis.get(emi) != null) {
			amis.get(emi).remove(getHead(recp));
		}
		if (amis.get(recp) != null) {
			amis.get(recp).remove(getHead(emi));
		}
	}

	public static void removePeticion(String emi, String recp) {
		if (pet.get(emi) != null) {
			pet.get(emi).remove(getHead(recp));
		}
	}

}
