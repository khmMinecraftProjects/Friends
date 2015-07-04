package me.khmdev.Friends.Gestores.Inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;
import me.khmdev.Friends.Gestores.InventoryFriendItem.friendType;

public class ItemOpenInventoryFriend extends CustomItem {
	private friendType type;
	private int init;
	private Player player;
	public ItemOpenInventoryFriend(ItemStack it,Player pl,friendType t,int p){
		super(it);
		type=t;
		init=p;
		player=pl;
	}
	@Override
	public void execute(InventoryClickEvent event) {
		Inventory inv=null;
		if(type==friendType.PET){
			inv=PeticionInventory.getInventory(player, init);
		}else{
			inv=FriendInventory.getInventory(player, init);
		}
		if(inv==null){
			event.getWhoClicked().closeInventory();
		}else{
			event.getWhoClicked().openInventory(inv);
		}
	}

	@Override
	public void execute(PlayerInteractEvent event) {
	}

}
