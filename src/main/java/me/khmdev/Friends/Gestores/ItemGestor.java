package me.khmdev.Friends.Gestores;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CItems;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomInventory;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;
import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.ItemOpenInventory;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.Friends.Base;
import me.khmdev.Friends.whereIs.SQLControl;

public class ItemGestor extends CustomItem {
	public ItemGestor(){
		super(AuxPlayer.getItem(Material.SKULL_ITEM, "Gestor de amigos",3));
	}
	@Override
	public void execute(InventoryClickEvent event) {

	}

	@Override
	public void execute(PlayerInteractEvent event) {
		Inventory inv=getInventory(event.getPlayer(),0);
		if(inv==null){event.getPlayer().sendMessage("Aun no tienes amigos");return;}
		event.getPlayer().openInventory(inv);
	}
	private static HashMap<String, CustomInventory>invs=new HashMap<>();
	private Inventory getInventory(Player pl,int principio) {
		List<String> friends = Base.getInstance().getGestor()
				.getFriends(pl.getName());
		if(principio>=friends.size()||
				principio<0){return null;}
		int i=principio;
		CustomInventory custom=invs.get(pl.getName());
		if(custom==null){
			custom=new CustomInventory("Amigos de "+pl.getName());
			CItems.addInventorys(custom);
			invs.put(pl.getName(), custom);
			Inventory inv = Bukkit.createInventory(pl, 6 * 9,"Amigos de "+pl.getName());
			custom.setInventory(inv);
		}		
		Inventory inv = custom.getInventory();
		inv.clear();
		
		
		int fin = principio+4*9;
		while(principio<friends.size()&&principio<fin){
			String s=friends.get(principio);

			ItemStack head = AuxPlayer.getItem(Material.SKULL_ITEM, s,3,
					SQLControl.getServer(s));
			inv.setItem(principio, head);
			principio++;
		}
		ItemStack bars=AuxPlayer.getItem(Material.IRON_FENCE, " ");
		for (int j = 0; j < 9; j++) {
			inv.setItem((4*9)+j, bars);
		}
		ItemOpenInventory anterior=
				new ItemOpenInventory(
				AuxPlayer.getItem(Material.REDSTONE_BLOCK, "Pagina anterior"),
				getInventory(pl, i-4*9)),
				siguiente=
				new ItemOpenInventory(
				AuxPlayer.getItem(Material.EMERALD_BLOCK, "Siguiente pagina"),
				getInventory(pl, i+4*9));
		inv.setItem(5*9, anterior.getItem());
		inv.setItem(5*9+1, anterior.getItem());
		inv.setItem(5*9+2, anterior.getItem());
		inv.setItem(5*9+3, anterior.getItem());

		inv.setItem(5*9+4,bars);

		inv.setItem(5*9+5, siguiente.getItem());
		inv.setItem(5*9+6, siguiente.getItem());
		inv.setItem(5*9+7, siguiente.getItem());
		inv.setItem(5*9+8, siguiente.getItem());

		custom.setInventory(inv);
		custom.addCustom(siguiente);
		custom.addCustom(anterior);
		return inv;
	}

}
