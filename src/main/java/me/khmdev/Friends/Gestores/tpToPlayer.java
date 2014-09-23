package me.khmdev.Friends.Gestores;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;
import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.APIAuxiliar.whereIs.ConstantesServerSQL;
import me.khmdev.APIAuxiliar.whereIs.SQLControl;
import me.khmdev.Friends.Base;
import me.khmdev.Friends.init;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class tpToPlayer extends CustomItem {
	private String player;

	public tpToPlayer(String pl) {
		super(AuxPlayer.getItem(Material.SKULL_ITEM, 
				"Teletransportarte",3,
				SQLControl.getServer(pl)));
		player = pl;
	}

	private static void tpToServer(Player pl, String serv) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(serv);
		pl.sendPluginMessage(Base.getInstance().getPlugin(), "BungeeCord",
				out.toByteArray());
	}

	@Override
	public void execute(InventoryClickEvent event) {
		Player pl = Bukkit.getServer().getPlayer(
				event.getWhoClicked().getUniqueId());
		if (pl == null) {
			return;
		}
		String sv1 = SQLControl.getServer(pl.getName()), sv2 = SQLControl
				.getServer(player);
		
		if (sv1 == sv2) {
			Player play=Bukkit.getPlayerExact(player);
			if(play==null){
				pl.sendMessage("No se ha podido teletransportar a " + player);
				return;
			}
			pl.teleport(play);
			pl.sendMessage("Teletransportando a " + player);

		} else {
			if (sv2 == ConstantesServerSQL.ServerNullName
					|| sv2 == SQLControl.desconocido) {
				pl.sendMessage(player + " esta desconectado");
				return;
			}
			if (init.isBungee()) {
				tpToServer(pl, sv2);
				pl.sendMessage("Teletransportando a " + player);

			} else {
				pl.sendMessage("No se puede teletransportar entre servidores");
			}
		}

	}

	@Override
	public void execute(PlayerInteractEvent event) {

	}

	public void updateServer() {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(SQLControl.getServer(player)));
		item.setItemMeta(meta);
	}
	
	public void updateServer(List<String> s) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(s);
		item.setItemMeta(meta);
	}
}
