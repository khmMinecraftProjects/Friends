package me.khmdev.Friends.Gestores.Items;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.APIAuxiliar.whereIs.ConstantesServerSQL;
import me.khmdev.APIAuxiliar.whereIs.SQLControl;
import me.khmdev.Friends.Base;
import me.khmdev.Friends.Gestores.InventoryFriendItem;
import me.khmdev.Friends.init;
import me.khmdev.Friends.lang.Lang;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class tpToPlayer extends IFriendItem {
	private String player;
	private long timeout = 5000;
	private static HashMap<String, Long> times = new HashMap<>();

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
	public void execute(Player pl, InventoryFriendItem inventory) {
		
		if(!pl.hasPermission("amigos.tp")){
			pl.sendMessage(Lang.get("tpToPlayer.noPerms"));
			return;
		}
		String name = pl.getName();
		if (times.containsKey(name)) {
			if ((System.currentTimeMillis() - times.get(name)) < timeout) {
				pl.sendMessage(Lang.get("tpToPlayer.reload"));
				return;
			} else {
				times.remove(name);
			}
		}
		String sv1 = SQLControl.getServer(pl.getName()), sv2 = SQLControl
				.getServer(player);
		
		if (sv1 == sv2) {
			Player play=Bukkit.getPlayerExact(player);
			if(play==null){
				pl.sendMessage(Lang.get("tpToPlayer.error")
						.replace("%player%", player));
				return;
			}
			pl.teleport(play);
			pl.sendMessage(Lang.get("tpToPlayer.tp")
					.replace("%player%", player));

		} else {
			if (sv2 == ConstantesServerSQL.ServerNullName
					|| sv2 == SQLControl.desconocido) {
				pl.sendMessage(Lang.get("tpToPlayer.offline")
						.replace("%player%", player));
				return;
			}
			if (init.isBungee()) {
				tpToServer(pl, sv2);
				pl.sendMessage(Lang.get("tpToPlayer.tp")
						.replace("%player%", player));

			} else {
				pl.sendMessage(Lang.get("tpToPlayer.serverError"));
			}
		}
		times.put(name, System.currentTimeMillis());
	}

	@Override
	public void update() {
		
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(SQLControl.getServer(player)));
		item.setItemMeta(meta);
	}

}
