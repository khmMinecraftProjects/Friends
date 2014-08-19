package me.khmdev.Friends.Gestores;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.khmdev.APIAuxiliar.Avisos.Avisos;
import me.khmdev.APIBase.Almacenes.sql.AlmacenSQL;
import me.khmdev.APIBase.Almacenes.sql.FieldSQL;
import me.khmdev.APIBase.Almacenes.sql.player.SQLPlayerData;
import me.khmdev.Friends.Constantes;

public class GestorSQL extends IGestor implements Runnable {
	private AlmacenSQL sql;
	private static int time=60;
	public GestorSQL(AlmacenSQL sq, JavaPlugin pl) {
		sql = sq;
		//actualizarFriends();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, this, 
				20*time, 20*time);
	}

	@Override
	public void actualizarFriends() {
		friends.clear();peticiones.clear();
		ResultSet r = sql.getValue(Constantes.tabla);
		if (r == null) {
			return;
		}
		try {
			while (r.next()) {
				String s = r.getString(Constantes.celdaEstado);
				if (s.equalsIgnoreCase(Constantes.tipoConfirmado)) {
					super.addFriend(r.getString(Constantes.celdaUser),
							r.getString(Constantes.celdaAmigo));
				} else if (s.equalsIgnoreCase(Constantes.tipoSinAceptar)) {
					super.addPeticion(r.getString(Constantes.celdaAmigo),
							r.getString(Constantes.celdaUser));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addFriend(String pl1, String pl2) {
		super.addFriend(pl1, pl2);
		super.removePeticion(pl1, pl2);

		sql.sendUpdate("UPDATE `" + Constantes.tabla + "` SET " + "`"
				+ Constantes.celdaEstado + "` =  '" + Constantes.tipoConfirmado
				+ "'" + " WHERE (`" + Constantes.celdaUser + "` LIKE '" + pl1
				+ "' AND" + "`" + Constantes.celdaAmigo + "` LIKE '" + pl2
				+ "') OR (`" + Constantes.celdaUser + "` LIKE '" + pl2
				+ "' AND" + "`" + Constantes.celdaAmigo + "` LIKE '" + pl1
				+ "')" + "\n");
		Avisos.addMensaje(pl2, pl1+" ha aceptado tu peticion de amistad");

	}

	public void removeFriend(String pl1, String pl2) {
		super.removeFriend(pl1, pl2);
		sql.sendUpdate("DELETE FROM `" + Constantes.tabla + "` WHERE (`"
				+ Constantes.celdaUser + "` LIKE '" + pl1 + "' AND" + "`"
				+ Constantes.celdaAmigo + "` LIKE '" + pl2 + "') OR (`"
				+ Constantes.celdaUser + "` LIKE '" + pl2 + "' AND" + "`"
				+ Constantes.celdaAmigo + "` LIKE '" + pl1 + "')" + "\n");
		Avisos.addMensaje(pl2, pl1+" ya no es tu amigo");

	}

	public void addPeticion(String pl1, String pl2) {
		super.addPeticion(pl1, pl2);

		sql.createField(Constantes.tabla, new FieldSQL(Constantes.celdaUser,
				pl1), new FieldSQL(Constantes.celdaAmigo, pl2), new FieldSQL(
				Constantes.celdaEstado, Constantes.tipoEsperando));

		sql.createField(Constantes.tabla, new FieldSQL(Constantes.celdaUser,
				pl2), new FieldSQL(Constantes.celdaAmigo, pl1), new FieldSQL(
				Constantes.celdaEstado, Constantes.tipoSinAceptar));
		
		Avisos.addMensaje(pl2, pl1 + " te ha enviado una peticion de amistad"
				+ "\nAcepta la peticion con  /amigos aceptar " + pl1
				+ "\nCancela la peticion con /amigos cancelar " + pl1);

	}

	public void removePeticion(String pl1, String pl2) {
		super.removePeticion(pl1, pl2);
		sql.sendUpdate("DELETE FROM `" + Constantes.tabla + "` where ("
				+ Constantes.celdaUser + " =" + pl1 + " AND" + ""
				+ Constantes.celdaAmigo + " =" + pl2 + ") OR ("
				+ Constantes.celdaUser + " =" + pl1 + " AND" + ""
				+ Constantes.celdaAmigo + " =" + pl2 + ")" + ")\n");
		Avisos.addMensaje(pl2, pl1
				+ " ha rechazado tu peticion de amistad");

	}

	@Override
	public void run() {
		actualizarFriends();
	}

	public boolean existe(String string) {
		return SQLPlayerData.existUser(string);
	}

}