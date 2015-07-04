package me.khmdev.Friends.Gestores;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CItems;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.ItemOpenInventory;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.APIAuxiliar.whereIs.SQLControl;
import me.khmdev.Friends.Base;
import me.khmdev.Friends.Gestores.Factorys.FactoryTPItem;
import me.khmdev.Friends.Gestores.Factorys.IFactoryFriendItem;
import me.khmdev.Friends.Gestores.Inventory.FriendCustomInventory;
import me.khmdev.Friends.Gestores.Items.AcceptPeticionItem;
import me.khmdev.Friends.Gestores.Items.AddFriendItem;
import me.khmdev.Friends.Gestores.Items.BackItem;
import me.khmdev.Friends.Gestores.Items.CancellPeticionItem;
import me.khmdev.Friends.Gestores.Items.RemoveFriendItem;

public class InventoryFriendItem extends ItemOpenInventory {
	private String player;
	public static enum friendType{FRIEND,KNOW,UNKNOW,PET};
	private FriendCustomInventory know, unknown, pet, friend;
	private static TreeMap<Integer,IFactoryFriendItem>StandarItems=new TreeMap<>();
	 static {
		 addFactory(new FactoryTPItem());
		 addFactory(new AddFriendItem());
		 addFactory(new RemoveFriendItem());
		 addFactory(new AcceptPeticionItem());
		 addFactory(new CancellPeticionItem());
		 addFactory(new BackItem());

	    }
	 public static void addFactory(IFactoryFriendItem fac){
		 StandarItems.put(fac.getPriority(),fac);
	 }
	public InventoryFriendItem(String pl) {
		super(AuxPlayer.getItem(Material.SKULL_ITEM, pl, 3,
				SQLControl.getServer(pl)), null);
		player = pl;
		know = new FriendCustomInventory("Gestion amigo %PLAYER%",this);
		friend = new FriendCustomInventory("Gestionar amigo %PLAYER%",this);
		unknown = new FriendCustomInventory("Gestionar %PLAYER%",this);
		pet = new FriendCustomInventory("Peticion de %PLAYER%",this);
		inventory = know.getInventory();
		for (Entry<Integer, IFactoryFriendItem> ent : StandarItems.descendingMap().entrySet()) {
			IFactoryFriendItem fac=ent.getValue();
			System.out.println(fac.create(pl));

			if(fac.show(friendType.KNOW)){
				know.addItem(fac.create(pl));
			}
			if(fac.show(friendType.UNKNOW)){
				unknown.addItem(fac.create(pl));
			}
			if(fac.show(friendType.FRIEND)){
				friend.addItem(fac.create(pl));
			}
			if(fac.show(friendType.PET)){
				pet.addItem(fac.create(pl));
			}
		}
		CItems.addInventorys(know);
		CItems.addInventorys(unknown);
		CItems.addInventorys(friend);
		CItems.addInventorys(pet);
	}

	public void update() {
		know.update();
		unknown.update();
		friend.update();
		pet.update();
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
	public Inventory getUlt(Player pl){
		return ult.get(pl);
	}
	public String getPlayer(){
		return player;
	}
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
