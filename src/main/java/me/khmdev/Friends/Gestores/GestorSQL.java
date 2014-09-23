package me.khmdev.Friends.Gestores;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.khmdev.APIAuxiliar.Avisos.Avisos;
import me.khmdev.APIBase.Almacenes.sql.AlmacenSQL;
import me.khmdev.APIBase.Almacenes.sql.Consulta;
import me.khmdev.APIBase.Almacenes.sql.FieldSQL;
import me.khmdev.APIBase.Almacenes.sql.player.SQLPlayerData;
import me.khmdev.Friends.ConstantesFriendSQL;

public class GestorSQL extends IGestor implements Runnable {
	private AlmacenSQL sql;
	private static int time=60;
	public GestorSQL(AlmacenSQL sq, JavaPlugin pl) {
		sql = sq;
		//actualizarFriends();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, this, 
				20, 20*time);
	}

	@Override
	public void actualizarFriends() {
		friends.clear();peticiones.clear();
		Consulta c = sql.getValue(ConstantesFriendSQL.tabla);
		
		if (c == null) {
			return;
		}
		ResultSet r=c.getR();
		try {
			while (r.next()) {
				String s = r.getString(ConstantesFriendSQL.celdaEstado);
				if (s.equalsIgnoreCase(ConstantesFriendSQL.tipoConfirmado)) {
					super.addFriend(r.getString(ConstantesFriendSQL.celdaUser),
							r.getString(ConstantesFriendSQL.celdaAmigo));
				} else if (s.equalsIgnoreCase(ConstantesFriendSQL.tipoSinAceptar)) {
					super.addPeticion(r.getString(ConstantesFriendSQL.celdaAmigo),
							r.getString(ConstantesFriendSQL.celdaUser));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			c.close();
		}
	}

	public void addFriend(String pl1, String pl2) {
		super.addFriend(pl1, pl2);
		super.removePeticion(pl1, pl2);

		sql.sendUpdate("UPDATE `" + ConstantesFriendSQL.tabla + "` SET " + "`"
				+ ConstantesFriendSQL.celdaEstado + "` =  '" + ConstantesFriendSQL.tipoConfirmado
				+ "'" + " WHERE (`" + ConstantesFriendSQL.celdaUser + "` LIKE '" + pl1
				+ "' AND" + "`" + ConstantesFriendSQL.celdaAmigo + "` LIKE '" + pl2
				+ "') OR (`" + ConstantesFriendSQL.celdaUser + "` LIKE '" + pl2
				+ "' AND" + "`" + ConstantesFriendSQL.celdaAmigo + "` LIKE '" + pl1
				+ "')" + "\n");
		Avisos.addMensaje(pl2, pl1+" ha aceptado tu peticion de amistad");

	}

	public void removeFriend(String pl1, String pl2) {
		super.removeFriend(pl1, pl2);
		sql.sendUpdate("DELETE FROM `" + ConstantesFriendSQL.tabla + "` WHERE (`"
				+ ConstantesFriendSQL.celdaUser + "` LIKE '" + pl1 + "' AND" + "`"
				+ ConstantesFriendSQL.celdaAmigo + "` LIKE '" + pl2 + "') OR (`"
				+ ConstantesFriendSQL.celdaUser + "` LIKE '" + pl2 + "' AND" + "`"
				+ ConstantesFriendSQL.celdaAmigo + "` LIKE '" + pl1 + "')" + "\n");
		Avisos.addMensaje(pl2, pl1+" ya no es tu amigo");

	}

	public void addPeticion(String pl1, String pl2) {
		super.addPeticion(pl1, pl2);

		sql.createField(ConstantesFriendSQL.tabla, new FieldSQL(ConstantesFriendSQL.celdaUser,
				pl1), new FieldSQL(ConstantesFriendSQL.celdaAmigo, pl2), new FieldSQL(
						ConstantesFriendSQL.celdaEstado, ConstantesFriendSQL.tipoEsperando));

		sql.createField(ConstantesFriendSQL.tabla, new FieldSQL(ConstantesFriendSQL.celdaUser,
				pl2), new FieldSQL(ConstantesFriendSQL.celdaAmigo, pl1), new FieldSQL(
						ConstantesFriendSQL.celdaEstado, ConstantesFriendSQL.tipoSinAceptar));
		
		Avisos.addMensaje(pl2, pl1 + " te ha enviado una peticion de amistad"
				+ "\nAcepta la peticion con  /amigos aceptar " + pl1
				+ "\nCancela la peticion con /amigos cancelar " + pl1);

	}

	public void removePeticion(String pl1, String pl2) {
		super.removePeticion(pl1, pl2);
		sql.sendUpdate("DELETE FROM `" + ConstantesFriendSQL.tabla + "` where ("
				+ ConstantesFriendSQL.celdaUser + " =" + pl1 + " AND" + ""
				+ ConstantesFriendSQL.celdaAmigo + " =" + pl2 + ") OR ("
				+ ConstantesFriendSQL.celdaUser + " =" + pl1 + " AND" + ""
				+ ConstantesFriendSQL.celdaAmigo + " =" + pl2 + ")" + ")\n");
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