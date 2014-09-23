package me.khmdev.Friends.Gestores;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CItems;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomInventory;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.ItemOpenInventory;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.APIAuxiliar.whereIs.SQLControl;
import me.khmdev.Friends.Base;
import me.khmdev.Friends.init;

public class InventoryFriendItem extends ItemOpenInventory {
	private String player;
	private CustomInventory know, unknown, pet, friend;
	private tpToPlayer it;

	public InventoryFriendItem(String pl) {
		super(AuxPlayer.getItem(Material.SKULL_ITEM, pl, 3,
				SQLControl.getServer(pl)), null);
		player = pl;
		know = new CustomInventory("Gestion amigo " + player);
		friend = new CustomInventory("Gestionar amigo " + player);
		unknown = new CustomInventory("Gestionar " + player);
		pet = new CustomInventory("Peticion de " + player);
		inventory = know.getInventory();

		it = new tpToPlayer(player);
		friend.addItem(it);
		CustomItem el = new CustomItem(AuxPlayer.getItem(
				Material.REDSTONE_BLOCK, "Eliminar amigo")) {
			@Override
			public void execute(PlayerInteractEvent event) {

			}

			@Override
			public void execute(InventoryClickEvent event) {

				if (!(event.getWhoClicked() instanceof Player)) {
					return;
				}
				Player sender = (Player) event.getWhoClicked();
				IGestor gestor = Base.getInstance().getGestor();
				List<String> l = gestor.getFriends(sender.getName());
				if (!l.contains(player)) {
					sender.sendMessage("No eres amigo de " + player);
					return;
				}
				gestor.removeFriend(sender.getName(), player);
				ItemGestor.removeFriend(sender.getName(),player);

				sender.sendMessage("Ya no eres amigo de " + player);
			}
		};
		know.addItem(el);
		friend.addItem(el);

		pet.addItem(new CustomItem(
				AuxPlayer.getItem(Material.EMERALD_BLOCK,
				"Aceptar peticion")) {

			@Override
			public void execute(PlayerInteractEvent event) {

			}

			@Override
			public void execute(InventoryClickEvent event) {
				if (!(event.getWhoClicked() instanceof Player)) {
					return;
				}
				Player sender = (Player) event.getWhoClicked();
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
				event.getWhoClicked().openInventory(ult.get(event.getWhoClicked()));

			}
		});
		pet.addItem(new CustomItem(AuxPlayer.getItem(
				Material.REDSTONE_BLOCK,
				"eliminar peticion")) {

			@Override
			public void execute(PlayerInteractEvent event) {

			}

			@Override
			public void execute(InventoryClickEvent event) {
				if (!(event.getWhoClicked() instanceof Player)) {
					return;
				}
				Player sender = (Player) event.getWhoClicked();
				IGestor gestor = Base.getInstance().getGestor();
				List<String> l2 = gestor.getPeticiones(sender.getName());

				if (l2.contains(sender.getName())) {
					sender.sendMessage(player
							+ " no te ha enviado ninguna peticion de amistad");
					return;
				}
				gestor.removePeticion(player, sender.getName());
				ItemGestor.removePeticion(sender.getName(),player);

				sender.sendMessage("Has eliminado la peticion de amistad de "
						+ player);
				event.getWhoClicked().openInventory(ult.get(event.getWhoClicked()));
			}
		});
		unknown.addItem(new CustomItem(AuxPlayer.getItem(
				Material.EMERALD_BLOCK, "Agregar amigo")) {
			@Override
			public void execute(PlayerInteractEvent event) {

			}

			@Override
			public void execute(InventoryClickEvent event) {

				if (!(event.getWhoClicked() instanceof Player)) {
					return;
				}
				Player sender = (Player) event.getWhoClicked();
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
		});
		if (init.hasPluging("Partys")) {
			know.addItem(new PartyItem(pl));
			friend.addItem(new PartyItem(pl));
			unknown.addItem(new PartyItem(pl));

		}
		CustomItem at = new CustomItem(AuxPlayer.getItem(Material.STICK,
				"Atras")) {

			@Override
			public void execute(PlayerInteractEvent event) {

			}

			@Override
			public void execute(InventoryClickEvent event) {
				if (event.getWhoClicked() instanceof Player) {
					event.getWhoClicked().openInventory(ult.get(event.getWhoClicked()));
				}
			}
		};
		friend.addItem(at);
		pet.addItem(at);

		CItems.addInventorys(know);
		CItems.addInventorys(unknown);
		CItems.addInventorys(friend);
		CItems.addInventorys(pet);

	}

	public void updateServer() {
		ItemMeta meta = item.getItemMeta();
		List<String> l = Arrays.asList(SQLControl.getServer(player));
		meta.setLore(l);
		item.setItemMeta(meta);
		it.updateServer(l);
	}

	@Override
	public void execute(InventoryClickEvent event) {
		event.setCancelled(true);
		if (inventory == null) {
			event.getWhoClicked().closeInventory();
			return;
		}
		ult.put((Player) event.getWhoClicked(), event.getInventory());
		if (Base.getInstance().getGestor()
				.sonAmigos(player, event.getWhoClicked().getName())) {
			event.getWhoClicked().openInventory(friend.getInventory());
		} else {

			event.getWhoClicked().openInventory(pet.getInventory());

		}
	}

	private HashMap<Player, Inventory>ult=new HashMap<>();
	
	@Override
	public void execute(PlayerInteractEvent event) {
		event.setCancelled(true);

	}

	public Inventory getUnknown() {
		return unknown.getInventory();
	}

	public Inventory getknown() {
		return know.getInventory();

	}
}
