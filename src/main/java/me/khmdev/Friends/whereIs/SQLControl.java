package me.khmdev.Friends.whereIs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.khmdev.APIBase.Almacenes.ConstantesAlmacen;
import me.khmdev.APIBase.Almacenes.sql.AlmacenSQL;
import me.khmdev.APIBase.Almacenes.sql.FieldSQL;
import me.khmdev.APIBase.Almacenes.sql.FieldSQLChange;
import me.khmdev.APIBase.Almacenes.sql.player.SQLPlayerData;
import me.khmdev.Friends.SQLConstantes;

public class SQLControl extends BukkitRunnable implements Listener {
	AlmacenSQL sql;
	String ip = "0.0.0.0", name;

	public SQLControl(AlmacenSQL sq) {
		sql = sq;
		for (String s : SQLConstantes.sql) {
			sql.sendUpdate(s);
		}

		name = Bukkit.getServerName();
		getIP();
		ip = ip + ":" + Bukkit.getServer().getPort();
		getServers();
		if (!servers.containsKey(ip) || servers.get(ip) != name) {
			try {
				ResultSet r = sql.getValue(SQLConstantes.tabla, new FieldSQL(
						SQLConstantes.ServidorID, ip));

				if (r != null && r.next()) {
					if (r.getString(SQLConstantes.ServidorAlias) != name) {
						sql.changeData(SQLConstantes.tabla, new FieldSQLChange(
								SQLConstantes.ServidorAlias, name, new FieldSQL(
										SQLConstantes.ServidorID, ip)));
					}
				} else {
					sql.createField(SQLConstantes.tabla, new FieldSQL(
							SQLConstantes.ServidorID, ip), new FieldSQL(
							SQLConstantes.ServidorAlias, name));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void getIP() {
		try {
			URL u = new URL("http://khmdev.esy.es/getIP.php");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					u.openStream()));
			ip = in.readLine();
		} catch (Exception ex) {
		}
	}

	@EventHandler
	public void logeIn(PlayerJoinEvent e) {
		String pl = e.getPlayer().getName();
		if (!SQLPlayerData.existUser(pl)) {
			SQLPlayerData.crearUser(pl, new FieldSQL(SQLConstantes.tabla, ip));
		} else {
			SQLPlayerData.setVar(pl, SQLConstantes.UserServidor, ip);
		}
	}

	@EventHandler
	public void logeOUT(PlayerQuitEvent e) {
		String pl = e.getPlayer().getName();
		SQLPlayerData.setVar(pl, SQLConstantes.UserServidor,
				SQLConstantes.ServerNull);
	}

	private static HashMap<String, String> servers = new HashMap<>();
	private static HashMap<String, String> players = new HashMap<>();

	private void getServers() {
		ResultSet r = sql.getValue(SQLConstantes.tabla);
		servers.clear();
		try {
			while (r.next()) {

				servers.put(r.getString(SQLConstantes.ServidorID),
						r.getString(SQLConstantes.ServidorAlias));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		ResultSet r = sql.getValue(ConstantesAlmacen.tabla);
		players.clear();
		try {
			while (r.next()) {
				String ip = r.getString(SQLConstantes.UserServidor);
				String name = servers.get(ip);
				if (name == null) {
					getServers();
					name = servers.get(ip);
				}
				players.put(r.getString(ConstantesAlmacen.id), name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String getServer(String player) {
		return players.get(player) != null ? players.get(player)
				: "Desconocido";
	}
}
