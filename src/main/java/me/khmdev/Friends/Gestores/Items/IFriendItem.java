package me.khmdev.Friends.Gestores.Items;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;
import me.khmdev.Friends.Gestores.InventoryFriendItem;

public abstract class IFriendItem extends CustomItem{
	
	public IFriendItem(ItemStack item) {
		super(item);
	}

	public abstract void execute(Player sender,InventoryFriendItem inventory);
	public void update() {
		
	}
	@Deprecated
	@Override
	public void execute(PlayerInteractEvent event) {}
	@Deprecated
	@Override
	public void execute(InventoryClickEvent event){};
	@Deprecated
	@Override
	public void execute(PlayerInteractEntityEvent event){};
	@Deprecated
	@Override
	public void execute(BlockPlaceEvent event){event.setCancelled(true);};
	
}
